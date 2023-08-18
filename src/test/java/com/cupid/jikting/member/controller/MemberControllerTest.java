package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.*;
import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.service.MemberService;
import com.cupid.jikting.member.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/v1";
    private static final String DOMAIN_ROOT_PATH = "/members";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final Long ID = 1L;
    private static final String USERNAME = "swm123";
    private static final String PASSWORD = "password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "01011112222";
    private static final String NICKNAME = "닉네임";
    private static final LocalDate BIRTH = LocalDate.of(1996, 5, 10);
    private static final String COMPANY = "직장";
    private static final String IMAGE_URL = "사진 URL";
    private static final int HEIGHT = 168;
    private static final String ADDRESS = "거주지";
    private static final String COLLEGE = "출신학교(선택사항 - 없을 시 빈 문자열)";
    private static final String PERSONALITY = "성격";
    private static final String HOBBY = "취미";
    private static final String DESCRIPTION = "한줄 소개(선택사항 - 없을 시 빈 문자열)";
    private static final String NEW_PASSWORD = "새 비밀번호";
    private static final String MEMBER_PROFILE_UPDATE_REQUEST_PARAMETER_NAME = "memberProfileUpdateRequest";
    private static final String MEMBER_PROFILE_UPDATE_REQUEST_FILENAME = "";
    private static final String MEMBER_PROFILE_UPDATE_REQUEST_CONTENT_TYPE = "application/json";
    private static final String IMAGE_PARAMETER_NAME = "file";
    private static final String IMAGE_FILENAME = "image.png";
    private static final String IMAGE_CONTENT_TYPE = "image/png";
    private static final String IMAGE_FILE = "이미지 파일";
    private static final String VERIFICATION_CODE = "인증번호";
    private static final String COMPANY_EMAIL = "회사 이메일";
    private static final String COMPANY_VERIFICATION_REQUEST_PARAMETER_NAME = "companyVerificationRequest";
    private static final String COMPANY_VERIFICATION_REQUEST_FILENAME = "";
    private static final String COMPANY_VERIFICATION_CONTENT_TYPE = "application/json";

    private String accessToken;
    private SignupRequest signupRequest;
    private NicknameUpdateRequest nicknameUpdateRequest;
    private MockMultipartFile memberProfileUpdateRequestPart;
    private PasswordUpdateRequest passwordUpdateRequest;
    private WithdrawRequest withdrawRequest;
    private UsernameCheckRequest usernameCheckRequest;
    private NicknameCheckRequest nicknameCheckRequest;
    private SignUpVerificationCodeRequest signUpVerificationCodeRequest;
    private UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest;
    private PhoneVerificationRequest phoneVerificationRequest;
    private VerificationRequest verificationRequest;
    private PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest;
    private PasswordResetRequest passwordResetRequest;
    private CompanyVerificationCodeRequest companyVerificationCodeRequest;
    private LoginRequest loginRequest;
    private MemberResponse memberResponse;
    private MemberProfileResponse memberProfileResponse;
    private UsernameResponse usernameResponse;
    private MockMultipartFile companyVerificationRequestPart;
    private MockMultipartFile image;
    private ApplicationException invalidFormatException;
    private ApplicationException memberNotFoundException;
    private ApplicationException idOrPasswordNotEqualException;
    private ApplicationException wrongFormException;
    private ApplicationException wrongFileExtensionException;
    private ApplicationException wrongFileSizeException;
    private ApplicationException verificationCodeNotEqualException;
    private ApplicationException duplicatedUsernameException;
    private ApplicationException duplicatedNicknameException;
    private ApplicationException verificationCodeExpiredException;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SmsService smsService;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        accessToken = jwtService.createAccessToken(ID);
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        Hobby hobby = Hobby.builder()
                .keyword(HOBBY)
                .build();
        List<MemberHobby> memberHobbies = IntStream.range(0, 3)
                .mapToObj(n -> MemberHobby.builder()
                        .hobby(hobby)
                        .build())
                .collect(Collectors.toList());
        Personality personality = Personality.builder()
                .keyword(PERSONALITY)
                .build();
        List<MemberPersonality> memberPersonalities = IntStream.range(0, 3)
                .mapToObj(n -> MemberPersonality.builder()
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
        Member member = Member.builder()
                .build();
        member.addMemberProfile(NICKNAME);
        ProfileImage profileImage = ProfileImage.builder()
                .id(ID)
                .sequence(Sequence.MAIN)
                .url(IMAGE_URL)
                .build();
        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateProfile(BIRTH, HEIGHT, Mbti.ENFJ, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                memberPersonalities, memberHobbies);
        MemberCompany memberCompany = MemberCompany.builder()
                .member(member)
                .company(company)
                .build();
        memberProfile.getMember().getMemberCompanies().add(memberCompany);
        List<ImageRequest> images = IntStream.range(0, 1)
                .mapToObj(n -> ImageRequest.builder()
                        .url(profileImage.getUrl())
                        .sequence(profileImage.getSequence().name())
                        .build())
                .collect(Collectors.toList());
        List<String> personalities = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());
        List<String> hobbies = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> HOBBY + n)
                .collect(Collectors.toList());
        CompanyVerificationRequest companyVerificationRequest = CompanyVerificationRequest.builder()
                .company(COMPANY)
                .build();
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .nickname(NICKNAME)
                .gender(Gender.MALE.getMessage())
                .build();
        nicknameUpdateRequest = NicknameUpdateRequest.builder()
                .nickname(NICKNAME)
                .build();
        MemberProfileUpdateRequest memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .birth(BIRTH)
                .height(HEIGHT)
                .gender(Gender.MALE.getMessage())
                .address(ADDRESS)
                .mbti(Mbti.ENFJ.name())
                .drinkStatus(DrinkStatus.OFTEN.getMessage())
                .smokeStatus(SmokeStatus.SMOKING.getMessage())
                .college(COLLEGE)
                .personalities(personalities)
                .hobbies(hobbies)
                .description(DESCRIPTION)
                .build();
        memberProfileUpdateRequestPart = new MockMultipartFile(MEMBER_PROFILE_UPDATE_REQUEST_PARAMETER_NAME, MEMBER_PROFILE_UPDATE_REQUEST_FILENAME, MEMBER_PROFILE_UPDATE_REQUEST_CONTENT_TYPE, toJson(memberProfileUpdateRequest).getBytes());
        passwordUpdateRequest = PasswordUpdateRequest.builder()
                .password(PASSWORD)
                .newPassword(NEW_PASSWORD)
                .build();
        withdrawRequest = WithdrawRequest.builder()
                .password(PASSWORD)
                .build();
        usernameCheckRequest = UsernameCheckRequest.builder()
                .username(USERNAME)
                .build();
        nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname(NICKNAME)
                .build();
        signUpVerificationCodeRequest = SignUpVerificationCodeRequest.builder()
                .to(PHONE)
                .build();
        usernameSearchVerificationCodeRequest = UsernameSearchVerificationCodeRequest.builder()
                .username(USERNAME)
                .phone(PHONE)
                .build();
        phoneVerificationRequest = PhoneVerificationRequest.builder()
                .phone(PHONE)
                .verificationCode(VERIFICATION_CODE)
                .build();
        verificationRequest = VerificationRequest.builder()
                .verificationCode(VERIFICATION_CODE)
                .build();
        passwordResetVerificationCodeRequest = PasswordResetVerificationCodeRequest.builder()
                .username(USERNAME)
                .name(NAME)
                .phone(PHONE)
                .build();
        passwordResetRequest = PasswordResetRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        companyVerificationCodeRequest = CompanyVerificationCodeRequest.builder()
                .company(COMPANY)
                .companyEmail(COMPANY_EMAIL)
                .build();
        loginRequest = LoginRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        memberResponse = MemberResponse.of(memberProfile);
        memberProfileResponse = MemberProfileResponse.of(memberProfile);
        usernameResponse = UsernameResponse.builder()
                .username(USERNAME)
                .build();
        companyVerificationRequestPart = new MockMultipartFile(COMPANY_VERIFICATION_REQUEST_PARAMETER_NAME, COMPANY_VERIFICATION_REQUEST_FILENAME, COMPANY_VERIFICATION_CONTENT_TYPE, toJson(companyVerificationRequest).getBytes(StandardCharsets.UTF_8));
        image = new MockMultipartFile(IMAGE_PARAMETER_NAME, IMAGE_FILENAME, IMAGE_CONTENT_TYPE, IMAGE_FILE.getBytes());
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        idOrPasswordNotEqualException = new NotEqualException(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD);
        wrongFormException = new WrongFormException(ApplicationError.INVALID_FORMAT);
        wrongFileExtensionException = new WrongFormException(ApplicationError.INVALID_FILE_EXTENSION);
        wrongFileSizeException = new WrongFormException(ApplicationError.INVALID_FILE_SIZE);
        verificationCodeNotEqualException = new NotEqualException(ApplicationError.VERIFICATION_CODE_NOT_EQUAL);
        duplicatedUsernameException = new DuplicateException(ApplicationError.DUPLICATE_USERNAME);
        duplicatedNicknameException = new DuplicateException(ApplicationError.DUPLICATE_NICKNAME);
        verificationCodeExpiredException = new BadRequestException(ApplicationError.VERIFICATION_CODE_EXPIRED);
    }

    @Test
    void 회원_가입_성공() throws Exception {
        // given
        willDoNothing().given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원_가입_요청();
        // then
        회원_가입_요청_성공(resultActions);
    }

    @Test
    void 회원_가입_실패() throws Exception {
        // given
        willThrow(invalidFormatException).given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원_가입_요청();
        // then
        회원_가입_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_조회_성공() throws Exception {
        // given
        willReturn(memberResponse).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원_조회_요청();
        // then
        회원_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원_조회_요청();
        // then
        회원_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_프로필_조회_성공() throws Exception {
        // given
        willReturn(memberProfileResponse).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원_프로필_조회_요청();
        // then
        회원_프로필_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_프로필_조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원_프로필_조회_요청();
        // then
        회원_프로필_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_닉네임_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).update(anyLong(), any(NicknameUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_닉네임_수정_요청();
        // then
        회원_닉네임_수정_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_닉네임_수정_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).update(anyLong(), any(NicknameUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_닉네임_수정_요청();
        // then
        회원_닉네임_수정_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_프로필_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).updateProfile(anyLong(), any(MultipartFile.class), any(MemberProfileUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_프로필_수정_요청();
        // then
        회원_프로필_수정_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_프로필_수정_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).updateProfile(anyLong(), any(MultipartFile.class), any(MemberProfileUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_프로필_수정_요청();
        // then
        회원_프로필_수정_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_비밀번호_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).updatePassword(anyLong(), any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_비밀번호_수정_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).updatePassword(anyLong(), any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_회원정보없음_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_비밀번호_수정_비밀번호불일치_실패() throws Exception {
        // given
        willThrow(idOrPasswordNotEqualException).given(memberService).updatePassword(anyLong(), any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_비밀번호불일치_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_비밀번호_수정_비밀번호양식불일치_실패() throws Exception {
        // given
        willThrow(wrongFormException).given(memberService).updatePassword(anyLong(), any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_비밀번호양식불일치_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_탈퇴_성공() throws Exception {
        // given
        willDoNothing().given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_탈퇴_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_회원정보없음_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_탈퇴_비밀번호불일치_실패() throws Exception {
        // given
        willThrow(idOrPasswordNotEqualException).given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_비밀번호불일치_실패(resultActions);
    }

    @Test
    void 아이디_중복_검사_성공() throws Exception {
        // given
        willDoNothing().given(memberService).checkDuplicatedUsername(any(UsernameCheckRequest.class));
        // when
        ResultActions resultActions = 아이디_중복_검사_요청();
        // then
        아이디_중복_검사_요청_성공(resultActions);
    }

    @Test
    void 아이디_중복_검사_실패() throws Exception {
        // given
        willThrow(duplicatedUsernameException).given(memberService).checkDuplicatedUsername(any(UsernameCheckRequest.class));
        // when
        ResultActions resultActions = 아이디_중복_검사_요청();
        // then
        아이디_중복_검사_요청_실패(resultActions);
    }

    @Test
    void 닉네임_중복_검사_성공() throws Exception {
        // given
        willDoNothing().given(memberService).checkDuplicatedNickname(any(NicknameCheckRequest.class));
        // when
        ResultActions resultActions = 닉네임_중복_검사_요청();
        // then
        닉네임_중복_검사_요청_성공(resultActions);
    }

    @Test
    void 닉네임_중복_검사_실패() throws Exception {
        // given
        willThrow(duplicatedNicknameException).given(memberService).checkDuplicatedUsername(any(UsernameCheckRequest.class));
        // when
        ResultActions resultActions = 아이디_중복_검사_요청();
        // then
        닉네임_중복_검사_요청_실패(resultActions);
    }

    @Test
    void 전화번호_인증번호_발급_성공() throws Exception {
        // given
        willDoNothing().given(smsService).createVerificationCodeForSignup(any(SignUpVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 전화번호_인증번호_발급_요청();
        // then
        전화번호_인증번호_발급_요청_성공(resultActions);
    }

    @Test
    void 전화번호_인증번호_발급_실패() throws Exception {
        // given
        willThrow(wrongFormException).given(smsService).createVerificationCodeForSignup(any(SignUpVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 전화번호_인증번호_발급_요청();
        // then
        전화번호_인증번호_발급_요청_실패(resultActions);
    }

    @Test
    void 전화번호_인증_성공() throws Exception {
        // given
        willDoNothing().given(memberService).verifyPhoneForSignup(any(PhoneVerificationRequest.class));
        // when
        ResultActions resultActions = 전화번호_인증_요청();
        // then
        전화번호_인증_요청_성공(resultActions);
    }

    @Test
    void 전화번호_인증_실패() throws Exception {
        // given
        willThrow(verificationCodeNotEqualException).given(memberService).verifyPhoneForSignup(any(PhoneVerificationRequest.class));
        // when
        ResultActions resultActions = 전화번호_인증_요청();
        // then
        전화번호_인증_요청_실패(resultActions);
    }

    @Test
    void 아이디_찾기_인증번호_발급_성공() throws Exception {
        // given
        willDoNothing().given(memberService).createVerificationCodeForSearchUsername(any(UsernameSearchVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 아이디_찾기_인증번호_발급_요청();
        // then
        아이디_찾기_인증번호_발급_요청_성공(resultActions);
    }

    @Test
    void 아이디_찾기_인증번호_발급_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).createVerificationCodeForSearchUsername(any(UsernameSearchVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 아이디_찾기_인증번호_발급_요청();
        // then
        아이디_찾기_인증번호_발급_요청_실패(resultActions);
    }

    @Test
    void 아이디_찾기_인증_성공() throws Exception {
        // given
        willReturn(usernameResponse).given(memberService).verifyForSearchUsername(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 아이디_찾기_인증_요청();
        // then
        아이디_찾기_인증_요청_성공(resultActions);
    }

    @Test
    void 아이디_찾기_인증_실패() throws Exception {
        // given
        willThrow(verificationCodeNotEqualException).given(memberService).verifyForSearchUsername(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 아이디_찾기_인증_요청();
        // then
        아이디_찾기_인증_요청_실패(resultActions);
    }

    @Test
    void 비밀번호_재설정_인증번호_발급_성공() throws Exception {
        // given
        willDoNothing().given(memberService).createVerificationCodeForResetPassword(any(PasswordResetVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_인증번호_발급_요청();
        // then
        비밀번호_재설정_인증번호_발급_요청_성공(resultActions);
    }

    @Test
    void 비밀번호_재설정_인증번호_발급_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).createVerificationCodeForResetPassword(any(PasswordResetVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_인증번호_발급_요청();
        // then
        비밀번호_재설정_인증번호_발급_요청_실패(resultActions);
    }

    @Test
    void 비밀번호_재설정_인증_성공() throws Exception {
        // given
        willDoNothing().given(memberService).verifyForResetPassword(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_인증_요청();
        // then
        비밀번호_재설정_인증_요청_성공(resultActions);
    }

    @Test
    void 비밀번호_재설정_인증_실패() throws Exception {
        // given
        willThrow(verificationCodeNotEqualException).given(memberService).verifyForResetPassword(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_인증_요청();
        // then
        비밀번호_재설정_인증_요청_실패(resultActions);
    }

    @Test
    void 비밀번호_재설정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).resetPassword(any(PasswordResetRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_요청();
        // then
        비밀번호_재설정_요청_성공(resultActions);
    }

    @Test
    void 비밀번호_재설정_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).resetPassword(any(PasswordResetRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_요청();
        // then
        비밀번호_재설정_요청_회원정보없음_실패(resultActions);
    }

    @Test
    void 비밀번호_재설정_비밀번호양식불일치_실패() throws Exception {
        // given
        willThrow(wrongFormException).given(memberService).resetPassword(any(PasswordResetRequest.class));
        // when
        ResultActions resultActions = 비밀번호_재설정_요청();
        // then
        비밀번호_재설정_요청_비밀번호양식불일치_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_이메일_인증번호_발급_성공() throws Exception {
        // given
        willDoNothing().given(memberService).createVerificationCodeForCompany(any(CompanyVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 회사_이메일_인증번호_발급_요청();
        // then
        회사_이메일_인증번호_발급_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_이메일_인증번호_발급_실패() throws Exception {
        // given
        willThrow(wrongFormException).given(memberService).createVerificationCodeForCompany(any(CompanyVerificationCodeRequest.class));
        // when
        ResultActions resultActions = 회사_이메일_인증번호_발급_요청();
        // then
        회사_이메일_인증번호_발급_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_이메일_인증_성공() throws Exception {
        // given
        willDoNothing().given(memberService).verifyForCompany(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 회사_이메일_인증_요청();
        // then
        회사_이메일_인증_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_이메일_인증_인증번호불일치_실패() throws Exception {
        // given
        willThrow(verificationCodeNotEqualException).given(memberService).verifyForCompany(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 회사_이메일_인증_요청();
        // then
        회사_이메일_인증_요청_인증번호불일치_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_이메일_인증_시간초과_실패() throws Exception {
        // given
        willThrow(verificationCodeExpiredException).given(memberService).verifyForCompany(any(VerificationRequest.class));
        // when
        ResultActions resultActions = 회사_이메일_인증_요청();
        // then
        회사_이메일_인증_요청_시간초과_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_명함_인증_성공() throws Exception {
        // given
        willDoNothing().given(memberService).verifyCardForCompany(any(CompanyVerificationRequest.class), any(MultipartFile.class));
        // when
        ResultActions resultActions = 회사_명함_인증_요청();
        // then
        회사_명함_인증_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_명함_인증_파일형식미지원_실패() throws Exception {
        // given
        willThrow(wrongFileExtensionException).given(memberService).verifyCardForCompany(any(CompanyVerificationRequest.class), any(MultipartFile.class));
        // when
        ResultActions resultActions = 회사_명함_인증_요청();
        // then
        회사_명함_인증_요청_파일형식미지원_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회사_명함_인증_파일크기미지원_실패() throws Exception {
        // given
        willThrow(wrongFileSizeException).given(memberService).verifyCardForCompany(any(CompanyVerificationRequest.class), any(MultipartFile.class));
        // when
        ResultActions resultActions = 회사_명함_인증_요청();
        // then
        회사_명함_인증_요청_파일크기미지원_실패(resultActions);
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        willDoNothing().given(memberService).login(any(LoginRequest.class));
        // when
        ResultActions resultActions = 로그인_요청();
        // then
        로그인_요청_성공(resultActions);
    }

    @Test
    void 로그인_실패() throws Exception {
        // given
        willThrow(idOrPasswordNotEqualException).given(memberService).login(any(LoginRequest.class));
        // when
        ResultActions resultActions = 로그인_요청();
        // then
        로그인_요청_실패(resultActions);
    }

    private ResultActions 회원_가입_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signupRequest)));
    }

    private void 회원_가입_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "signup-success");
    }

    private void 회원_가입_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "signup-fail");
    }

    private ResultActions 회원_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH));
    }

    private void 회원_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(memberResponse))),
                "get-member-success");
    }

    private void 회원_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "get-member-fail");
    }

    private ResultActions 회원_프로필_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/profile")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH));
    }

    private void 회원_프로필_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(memberProfileResponse))),
                "get-member-profile-success");
    }

    private void 회원_프로필_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "get-member-profile-fail");
    }

    private ResultActions 회원_닉네임_수정_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(nicknameUpdateRequest)));
    }

    private void 회원_닉네임_수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-nickname-success");
    }

    private void 회원_닉네임_수정_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "update-member-nickname-fail");
    }

    private ResultActions 회원_프로필_수정_요청() throws Exception {
        return mockMvc.perform(multipart(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/profile")
                .file(image)
                .file(memberProfileUpdateRequestPart)
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    private void 회원_프로필_수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-profile-success");
    }

    private void 회원_프로필_수정_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "update-member-profile-fail");
    }

    private ResultActions 회원_비밀번호_수정_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/password")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(passwordUpdateRequest)));
    }

    private void 회원_비밀번호_수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-password-success");
    }

    private void 회원_비밀번호_수정_요청_회원정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "update-member-password-not-found-member-fail");
    }

    private void 회원_비밀번호_수정_요청_비밀번호불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(idOrPasswordNotEqualException)))),
                "update-member-password-not-equal-password-fail");
    }

    private void 회원_비밀번호_수정_요청_비밀번호양식불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "update-member-password-wrong-form-fail");
    }

    private ResultActions 회원_탈퇴_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/withdraw")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(withdrawRequest)));
    }

    private void 회원_탈퇴_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "delete-member-success");
    }

    private void 회원_탈퇴_요청_회원정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "delete-member-not-found-member-fail");
    }

    private void 회원_탈퇴_요청_비밀번호불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(idOrPasswordNotEqualException)))),
                "delete-member-not-equal-password-fail");
    }

    private ResultActions 아이디_중복_검사_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/username/check")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(usernameCheckRequest)));
    }

    private void 아이디_중복_검사_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "check-duplicated-username-success");
    }

    private void 아이디_중복_검사_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(duplicatedUsernameException)))),
                "check-duplicated-username-fail");
    }

    private ResultActions 닉네임_중복_검사_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/nickname/check")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(nicknameCheckRequest)));
    }

    private void 닉네임_중복_검사_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "check-duplicated-nickname-success");
    }

    private void 닉네임_중복_검사_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(duplicatedNicknameException)))),
                "check-duplicated-nickname-fail");
    }

    private ResultActions 전화번호_인증번호_발급_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/code")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signUpVerificationCodeRequest)));
    }

    private void 전화번호_인증번호_발급_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "signup-create-verification-code-success");
    }

    private void 전화번호_인증번호_발급_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "signup-create-verification-code-fail");
    }

    private ResultActions 전화번호_인증_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/verification")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(phoneVerificationRequest)));
    }

    private void 전화번호_인증_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "signup-verify-phone-success");
    }

    private void 전화번호_인증_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(verificationCodeNotEqualException)))),
                "signup-verify-phone-fail");
    }

    private ResultActions 아이디_찾기_인증번호_발급_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/username/search/code")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(usernameSearchVerificationCodeRequest)));
    }

    private void 아이디_찾기_인증번호_발급_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "search-username-create-verification-code-success");
    }

    private void 아이디_찾기_인증번호_발급_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "search-username-create-verification-code-fail");
    }

    private ResultActions 아이디_찾기_인증_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/username/search/verification")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(verificationRequest)));
    }

    private void 아이디_찾기_인증_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(usernameResponse))),
                "search-username-verify-success");
    }

    private void 아이디_찾기_인증_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(verificationCodeNotEqualException)))),
                "search-username-verify-fail");
    }

    private ResultActions 비밀번호_재설정_인증번호_발급_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/password/reset/code")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(passwordResetVerificationCodeRequest)));
    }

    private void 비밀번호_재설정_인증번호_발급_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "reset-password-create-verification-code-success");
    }

    private void 비밀번호_재설정_인증번호_발급_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "reset-password-create-verification-code-fail");
    }

    private ResultActions 비밀번호_재설정_인증_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/password/reset/verification")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(verificationRequest)));
    }

    private void 비밀번호_재설정_인증_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "reset-password-verify-success");
    }

    private void 비밀번호_재설정_인증_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(verificationCodeNotEqualException)))),
                "reset-password-verify-fail");
    }

    private ResultActions 비밀번호_재설정_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/password/reset")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(passwordResetRequest)));
    }

    private void 비밀번호_재설정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "reset-password-success");
    }

    private void 비밀번호_재설정_요청_회원정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "reset-password-not-found-member-fail");
    }

    private void 비밀번호_재설정_요청_비밀번호양식불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "reset-password-wrong-form-fail");
    }

    private ResultActions 회사_이메일_인증번호_발급_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/company/code")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(companyVerificationCodeRequest)));
    }

    private void 회사_이메일_인증번호_발급_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "verify-company-create-verification-code-success");
    }

    private void 회사_이메일_인증번호_발급_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "verify-company-create-verification-code-fail");
    }

    private ResultActions 회사_이메일_인증_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/company/verification")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(verificationRequest)));
    }

    private void 회사_이메일_인증_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "verify-company-success");
    }

    private void 회사_이메일_인증_요청_인증번호불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(verificationCodeNotEqualException)))),
                "verify-company-not-equal-code-fail");
    }

    private void 회사_이메일_인증_요청_시간초과_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(verificationCodeExpiredException)))),
                "verify-company-code-expired-fail");
    }

    private ResultActions 회사_명함_인증_요청() throws Exception {
        return mockMvc.perform(multipart(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/company/card")
                .file(companyVerificationRequestPart)
                .file(image)
                .header(AUTHORIZATION, BEARER + accessToken)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    private void 회사_명함_인증_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "verify-company-card-success");
    }

    private void 회사_명함_인증_요청_파일형식미지원_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFileExtensionException)))),
                "verify-company-card-invalid-file-extension-fail");
    }

    private void 회사_명함_인증_요청_파일크기미지원_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFileSizeException)))),
                "verify-company-card-invalid-file-size-fail");
    }

    private ResultActions 로그인_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/login")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(loginRequest)));
    }

    private void 로그인_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "login-success");
    }

    private void 로그인_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(idOrPasswordNotEqualException)))),
                "login-fail");
    }
}
