package com.cupid.jikting.member.service;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.repository.PersonalityRepository;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.repository.CompanyRepository;
import com.cupid.jikting.member.repository.HobbyRepository;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private static final String EMAIL_DELIMITER = "@";
    private static final int DOMAIN = 1;

    private final FileUploadService fileUploadService;
    private final SmsService smsService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final CompanyRepository companyRepository;
    private final PersonalityRepository personalityRepository;
    private final HobbyRepository hobbyRepository;
    private final RedisConnector redisConnector;

    public void signup(SignupRequest signupRequest) {
        Member member = Member.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .phone(signupRequest.getPhone())
                .gender(Gender.find(signupRequest.getGender()))
                .name(signupRequest.getName())
                .role(Role.UNCERTIFIED)
                .socialType(SocialType.NORMAL)
                .build();
        member.addMemberProfile(signupRequest.getNickname());
        memberRepository.save(member);
    }

    public MemberResponse get(Long memberProfileId) {
        return MemberResponse.of(getMemberProfileById(memberProfileId));
    }

    public MemberProfileResponse getProfile(Long memberProfileId) {
        return MemberProfileResponse.of(getMemberProfileById(memberProfileId));
    }

    public void update(Long memberProfileId, NicknameUpdateRequest nicknameUpdateRequest) {
        MemberProfile memberProfile = getMemberProfileById(memberProfileId);
        memberProfile.update(nicknameUpdateRequest.getNickname());
        memberProfileRepository.save(memberProfile);
    }

    public void updateProfile(Long memberProfileId, MultipartFile file,
                              MemberProfileUpdateRequest memberProfileUpdateRequest) throws IOException {
        MemberProfile memberProfile = getMemberProfileById(memberProfileId);
        memberProfile.updateProfile(memberProfileUpdateRequest.getBirth(),
                memberProfileUpdateRequest.getHeight(),
                Mbti.valueOf(memberProfileUpdateRequest.getMbti()),
                memberProfileUpdateRequest.getAddress(),
                memberProfileUpdateRequest.getCollege(),
                SmokeStatus.find(memberProfileUpdateRequest.getSmokeStatus()),
                DrinkStatus.find(memberProfileUpdateRequest.getDrinkStatus()),
                memberProfileUpdateRequest.getDescription(),
                getMemberPersonalities(memberProfile, memberProfileUpdateRequest.getPersonalities()),
                getMemberHobbies(memberProfile, memberProfileUpdateRequest.getHobbies()));
        if (file != null && !file.isEmpty()) {
            ImageRequest imageRequest = ImageRequest.builder()
                    .url(fileUploadService.update(file, memberProfile.getMainImageUrl()))
                    .sequence(Sequence.MAIN.name())
                    .build();
            memberProfile.updateProfileImage(List.of(imageRequest));
        }
        memberProfileRepository.save(memberProfile);
    }

    public void updatePassword(Long memberProfileId, PasswordUpdateRequest passwordUpdateRequest) {
        MemberProfile memberProfile = getMemberProfileById(memberProfileId);
        memberProfile.validatePassword(passwordEncoder, passwordUpdateRequest.getPassword());
        memberProfile.updatePassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
    }

    public void withdraw(Long memberProfileId, WithdrawRequest withdrawRequest) {
        Member member = getMemberProfileById(memberProfileId).getMember();
        member.validatePassword(passwordEncoder, withdrawRequest.getPassword());
        memberRepository.delete(member);
    }

    public void checkDuplicatedUsername(UsernameCheckRequest usernameCheckRequest) {
        if (memberRepository.existsByUsername(usernameCheckRequest.getUsername())) {
            throw new DuplicateException(ApplicationError.DUPLICATE_USERNAME);
        }
    }

    public void checkDuplicatedNickname(NicknameCheckRequest nicknameCheckRequest) {
        if (memberProfileRepository.existsByNickname(nicknameCheckRequest.getNickname())) {
            throw new DuplicateException(ApplicationError.DUPLICATE_NICKNAME);
        }
    }

    public void createVerificationCodeForSignup(SignUpVerificationCodeRequest signUpVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        smsService.sendSms(SendSmsRequest.from(signUpVerificationCodeRequest.getPhone()));
    }

    public void verifyPhoneForSignup(PhoneVerificationRequest verificationRequest) {
        validateVerificationCode(verificationRequest.getPhone(), verificationRequest.getVerificationCode());
    }

    public void createVerificationCodeForSearchUsername(UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (!memberRepository.existsByNameAndPhone(usernameSearchVerificationCodeRequest.getName(), usernameSearchVerificationCodeRequest.getPhone())) {
            throw new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        }
        smsService.sendSms(SendSmsRequest.from(usernameSearchVerificationCodeRequest.getPhone()));
    }

    public UsernameResponse verifyForSearchUsername(VerificationPhoneRequest verificationPhoneRequest) {
        validateVerificationCode(verificationPhoneRequest.getPhone(), verificationPhoneRequest.getVerificationCode());
        return memberRepository.findByPhone(verificationPhoneRequest.getPhone())
                .map(UsernameResponse::from)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    public void createVerificationCodeForResetPassword(PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (!memberRepository.existsByUsernameAndNameAndPhone(passwordResetVerificationCodeRequest.getUsername(), passwordResetVerificationCodeRequest.getName(), passwordResetVerificationCodeRequest.getPhone())) {
            throw new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        }
        smsService.sendSms(SendSmsRequest.from(passwordResetVerificationCodeRequest.getPhone()));
    }

    public void verifyForResetPassword(VerificationPhoneRequest verificationPhoneRequest) {
        validateVerificationCode(verificationPhoneRequest.getPhone(), verificationPhoneRequest.getVerificationCode());
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        Member member = getMemberByUsername(passwordResetRequest.getUsername());
        member.updatePassword(passwordResetRequest.getPassword());
        memberRepository.save(member);
    }

    public void createVerificationCodeForCompany(Long memberProfileId, CompanyVerificationCodeRequest companyVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String email = companyVerificationCodeRequest.getEmail();
        validateCompanyDomain(email);
        mailService.sendMail(getMemberProfileById(memberProfileId).getMemberName(), email);
    }

    public void verifyForCompany(VerificationEmailRequest verificationPhoneRequest) {
        String email = verificationPhoneRequest.getEmail();
        validateCompanyDomain(email);
        validateVerificationCode(email, verificationPhoneRequest.getVerificationCode());
    }

    public void verifyCardForCompany(CompanyVerificationRequest companyVerificationRequest, MultipartFile multipartFile) {
    }

    public void login(LoginRequest loginRequest) {
    }

    public void blockCompany(Long memberProfileId) {
        getMemberProfileById(memberProfileId).getMember().blockCompanies();
    }

    public void report(Long memberProfileId, Long reportMemberProfileId, ReportMessageRequest reportMessageRequest) {
    }

    private MemberProfile getMemberProfileById(Long memberProfileId) {
        return memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    private List<MemberPersonality> getMemberPersonalities(MemberProfile memberProfile, List<String> keywords) {
        return keywords.stream()
                .map(this::getPersonalityByKeyword)
                .map(personality -> MemberPersonality.builder()
                        .memberProfile(memberProfile)
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
    }

    private Personality getPersonalityByKeyword(String keyword) {
        return personalityRepository.findByKeyword(keyword)
                .orElseThrow(() -> new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND));
    }

    private List<MemberHobby> getMemberHobbies(MemberProfile memberProfile, List<String> keywords) {
        return keywords.stream()
                .map(this::getHobbyByKeyword)
                .map(hobby -> MemberHobby.builder()
                        .memberProfile(memberProfile)
                        .hobby(hobby)
                        .build())
                .collect(Collectors.toList());
    }

    private Hobby getHobbyByKeyword(String keyword) {
        return hobbyRepository.findByKeyword(keyword)
                .orElseThrow(() -> new NotFoundException(ApplicationError.HOBBY_NOT_FOUND));
    }

    private void validateVerificationCode(String key, String verificationCode) {
        if (!redisConnector.get(key).equals(verificationCode)) {
            throw new BadRequestException(ApplicationError.VERIFICATION_CODE_NOT_EQUAL);
        }
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    private void validateCompanyDomain(String email) {
        if (!companyRepository.existsByEmail(extractDomain(email))) {
            throw new NotFoundException(ApplicationError.INVALID_COMPANY);
        }
    }

    private String extractDomain(String email) {
        return email.split(EMAIL_DELIMITER)[DOMAIN];
    }
}
