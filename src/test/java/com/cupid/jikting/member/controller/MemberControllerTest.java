package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.*;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/members";
    private static final String USERNAME = "아이디";
    private static final String PASSWORD = "비밀번호";
    private static final String NAME = "이름";
    private static final String PHONE = "전화번호";
    private static final String NICKNAME = "닉네임";
    private static final String COMPANY = "직장";
    private static final String IMAGE_URL = "사진 URL";
    private static final int AGE = 21;
    private static final int HEIGHT = 168;
    private static final String GENDER = "성별";
    private static final String ADDRESS = "거주지";
    private static final String MBTI = "MBTI";
    private static final String DRINK_STATUS = "음주여부";
    private static final boolean IS_SMOKE = false;
    private static final String COLLEGE = "출신학교(선택사항 - 없을 시 빈 문자열)";
    private static final String PERSONALITY = "성격";
    private static final String HOBBY = "취미";
    private static final String DESCRIPTION = "한줄 소개(선택사항 - 없을 시 빈 문자열)";
    private static final String SEQUENCE = "순서";
    private static final String NEW_PASSWORD = "새 비밀번호";
    private static final String FILE_HEADER = "file";
    private static final String ORIGINAL_FILENAME = "image.png";
    private static final String CONTENT_TYPE = "image/png";
    private static final String IMAGE_FILE = "이미지 파일";
    private static final String VERIFICATION_CODE = "인증번호";

    private SignupRequest signupRequest;
    private NicknameUpdateRequest nicknameUpdateRequest;
    private MemberProfileUpdateRequest memberProfileUpdateRequest;
    private PasswordUpdateRequest passwordUpdateRequest;
    private WithdrawRequest withdrawRequest;
    private UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest;
    private VerificationRequest verificationRequest;
    private PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest;
    private PasswordResetRequest passwordResetRequest;
    private MemberResponse memberResponse;
    private MemberProfileResponse memberProfileResponse;
    private UsernameResponse usernameResponse;
    private ApplicationException invalidFormatException;
    private ApplicationException memberNotFoundException;
    private ApplicationException passwordNotEqualException;
    private ApplicationException wrongFormException;
    private ApplicationException wrongFileExtensionException;
    private ApplicationException wrongFileSizeException;
    private ApplicationException verificationCodeNotEqualException;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        List<ImageResponse> images = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> ImageResponse.builder()
                        .url(IMAGE_URL)
                        .sequence(SEQUENCE)
                        .build())
                .collect(Collectors.toList());
        List<String> personalities = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());
        List<String> hobbies = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> HOBBY + n)
                .collect(Collectors.toList());
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
        nicknameUpdateRequest = NicknameUpdateRequest.builder()
                .nickname(NICKNAME)
                .build();
        memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .images(images)
                .age(AGE)
                .height(HEIGHT)
                .gender(GENDER)
                .address(ADDRESS)
                .mbti(MBTI)
                .drinkStatus(DRINK_STATUS)
                .isSmoke(IS_SMOKE)
                .college(COLLEGE)
                .personalities(personalities)
                .hobbies(hobbies)
                .description(DESCRIPTION)
                .build();
        passwordUpdateRequest = PasswordUpdateRequest.builder()
                .password(PASSWORD)
                .newPassword(NEW_PASSWORD)
                .build();
        withdrawRequest = WithdrawRequest.builder()
                .password(PASSWORD)
                .build();
        usernameSearchVerificationCodeRequest = UsernameSearchVerificationCodeRequest.builder()
                .username(USERNAME)
                .phone(PHONE)
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
        memberResponse = MemberResponse.builder()
                .nickname(NICKNAME)
                .company(COMPANY)
                .imageUrl(IMAGE_URL)
                .build();
        memberProfileResponse = MemberProfileResponse.builder()
                .images(images)
                .age(AGE)
                .height(HEIGHT)
                .gender(GENDER)
                .address(ADDRESS)
                .mbti(MBTI)
                .drinkStatus(DRINK_STATUS)
                .isSmoke(IS_SMOKE)
                .college(COLLEGE)
                .personalities(personalities)
                .hobbies(hobbies)
                .description(DESCRIPTION)
                .build();
        usernameResponse = UsernameResponse.builder()
                .username(USERNAME)
                .build();
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        passwordNotEqualException = new NotEqualException(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD);
        wrongFormException = new WrongFromException(ApplicationError.INVALID_FORMAT);
        wrongFileExtensionException = new WrongFromException(ApplicationError.INVALID_FILE_EXTENSION);
        wrongFileSizeException = new WrongFromException(ApplicationError.INVALID_FILE_SIZE);
        verificationCodeNotEqualException = new NotEqualException(ApplicationError.VERIFICATION_CODE_NOT_EQUAL);
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

    @Test
    void 회원_조회_성공() throws Exception {
        // given
        willReturn(memberResponse).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원_조회_요청();
        // then
        회원_조회_요청_성공(resultActions);
    }

    @Test
    void 회원_조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원_조회_요청();
        // then
        회원_조회_요청_실패(resultActions);
    }

    @Test
    void 회원_프로필_조회_성공() throws Exception {
        // given
        willReturn(memberProfileResponse).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원_프로필_조회_요청();
        // then
        회원_프로필_조회_요청_성공(resultActions);
    }

    @Test
    void 회원_프로필_조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원_프로필_조회_요청();
        // then
        회원_프로필_조회_요청_실패(resultActions);
    }

    @Test
    void 회원_닉네임_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).update(any(NicknameUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_닉네임_수정_요청();
        // then
        회원_닉네임_수정_요청_성공(resultActions);
    }

    @Test
    void 회원_닉네임수정_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).update(any(NicknameUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_닉네임_수정_요청();
        // then
        회원_닉네임_수정_요청_실패(resultActions);
    }

    @Test
    void 회원_프로필_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).updateProfile(any(MemberProfileUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_프로필_수정_요청();
        // then
        회원_프로필_수정_요청_성공(resultActions);
    }

    @Test
    void 회원_프로필_수정_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).updateProfile(any(MemberProfileUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_프로필_수정_요청();
        // then
        회원_프로필_수정_요청_실패(resultActions);
    }

    @Test
    void 회원_비밀번호_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).updatePassword(any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_성공(resultActions);
    }

    @Test
    void 회원_비밀번호_수정_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).updatePassword(any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_회원정보없음_실패(resultActions);
    }

    @Test
    void 회원_비밀번호_수정_비밀번호불일치_실패() throws Exception {
        // given
        willThrow(passwordNotEqualException).given(memberService).updatePassword(any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_비밀번호불일치_실패(resultActions);
    }

    @Test
    void 회원_비밀번호_수정_비밀번호양식불일치_실패() throws Exception {
        // given
        willThrow(wrongFormException).given(memberService).updatePassword(any(PasswordUpdateRequest.class));
        // when
        ResultActions resultActions = 회원_비밀번호_수정_요청();
        // then
        회원_비밀번호_수정_요청_비밀번호양식불일치_실패(resultActions);
    }

    @Test
    void 회원_이미지_수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).updateImage(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_성공(resultActions);
    }

    @Test
    void 회원_이미지_수정_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).updateImage(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_회원정보없음_실패(resultActions);
    }

    @Test
    void 회원_이미지_수정_파일형식미지원_실패() throws Exception {
        // given
        willThrow(wrongFileExtensionException).given(memberService).updateImage(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_파일형식미지원_실패(resultActions);
    }

    @Test
    void 회원_이미지_수정_파일크기미지원_실패() throws Exception {
        // given
        willThrow(wrongFileSizeException).given(memberService).updateImage(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_파일크기미지원_실패(resultActions);
    }

    @Test
    void 회원_탈퇴_성공() throws Exception {
        // given
        willDoNothing().given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_성공(resultActions);
    }

    @Test
    void 회원_탈퇴_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_회원정보없음_실패(resultActions);
    }

    @Test
    void 회원_탈퇴_비밀번호불일치_실패() throws Exception {
        // given
        willThrow(passwordNotEqualException).given(memberService).withdraw(anyLong(), any(WithdrawRequest.class));
        // when
        ResultActions resultActions = 회원_탈퇴_요청();
        // then
        회원_탈퇴_요청_비밀번호불일치_실패(resultActions);
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
    void 아이디_찾기_실패() throws Exception {
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
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/profile")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(memberProfileUpdateRequest)));
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
                        .andExpect(content().json(toJson(ErrorResponse.from(passwordNotEqualException)))),
                "update-member-password-not-equal-password-fail");
    }

    private void 회원_비밀번호_수정_요청_비밀번호양식불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "update-member-password-wrong-form-fail");
    }

    private ResultActions 회원_이미지_수정_요청() throws Exception {
        return mockMvc.perform(multipart(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/image")
                .file(new MockMultipartFile(FILE_HEADER, ORIGINAL_FILENAME, CONTENT_TYPE, IMAGE_FILE.getBytes()))
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    private void 회원_이미지_수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-image-success");
    }

    private void 회원_이미지_수정_요청_회원정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "update-member-image-not-found-member-fail");
    }

    private void 회원_이미지_수정_요청_파일형식미지원_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFileExtensionException)))),
                "update-member-image-invalid-file-extension-fail");
    }

    private void 회원_이미지_수정_요청_파일크기미지원_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFileSizeException)))),
                "update-member-image-invalid-file-size-fail");
    }

    private ResultActions 회원_탈퇴_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/withdraw")
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
                        .andExpect(content().json(toJson(ErrorResponse.from(passwordNotEqualException)))),
                "delete-member-not-equal-password-fail");
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
                "reset-password-fail");
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
}
