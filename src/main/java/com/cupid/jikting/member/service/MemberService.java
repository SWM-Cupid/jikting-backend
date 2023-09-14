package com.cupid.jikting.member.service;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.*;
import com.cupid.jikting.common.repository.PersonalityRepository;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.entity.*;
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

    private static final String SMS_SEND_SUCCESS = "202";

    private final FileUploadService fileUploadService;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
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
            throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .to(signUpVerificationCodeRequest.getPhone())
                .build();
        if (!smsService.sendSms(sendSmsRequest).getStatusCode().equals(SMS_SEND_SUCCESS)) {
            throw new SmsSendFailException(ApplicationError.SMS_SEND_FAIL);
        }
    }

    public void verifyPhoneForSignup(PhoneVerificationRequest verificationRequest) {
        validateVerificationCode(verificationRequest.getPhone(), verificationRequest.getVerificationCode());
    }

    public void createVerificationCodeForSearchUsername(UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (!memberRepository.existsByNameAndPhone(usernameSearchVerificationCodeRequest.getName(), usernameSearchVerificationCodeRequest.getPhone())) {
            throw new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        }
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .to(usernameSearchVerificationCodeRequest.getPhone())
                .build();
        if (!smsService.sendSms(sendSmsRequest).getStatusCode().equals(SMS_SEND_SUCCESS)) {
            throw new SmsSendFailException(ApplicationError.SMS_SEND_FAIL);
        }
    }

    public UsernameResponse verifyForSearchUsername(VerificationRequest verificationRequest) {
        validateVerificationCode(verificationRequest.getPhone(), verificationRequest.getVerificationCode());
        return memberRepository.findByPhone(verificationRequest.getPhone())
                .map(UsernameResponse::from)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    public void createVerificationCodeForResetPassword(PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (!memberRepository.existsByUsernameAndNameAndPhone(passwordResetVerificationCodeRequest.getUsername(), passwordResetVerificationCodeRequest.getName(), passwordResetVerificationCodeRequest.getPhone())) {
            throw new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        }
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .to(passwordResetVerificationCodeRequest.getPhone())
                .build();
        if (!smsService.sendSms(sendSmsRequest).getStatusCode().equals(SMS_SEND_SUCCESS)) {
            throw new SmsSendFailException(ApplicationError.SMS_SEND_FAIL);
        }
    }

    public void verifyForResetPassword(VerificationRequest verificationRequest) {
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
    }

    public void createVerificationCodeForCompany(CompanyVerificationCodeRequest companyVerificationCodeRequest) {
    }

    public void verifyForCompany(VerificationRequest verificationRequest) {
    }

    public void verifyCardForCompany(CompanyVerificationRequest companyVerificationRequest, MultipartFile multipartFile) {
    }

    public void login(LoginRequest loginRequest) {
    }

    public void blockCompany(Long memberProfileId) {
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

    private void validateVerificationCode(String phone, String verificationCode) {
        if (!redisConnector.get(phone).equals(verificationCode)) {
            throw new BadRequestException(ApplicationError.VERIFICATION_CODE_NOT_EQUAL);
        }
    }
}
