package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.error.WrongFormException;
import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.member.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/v1";
    private static final String DOMAIN_ROOT_PATH = "/files";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final Long ID = 1L;
    private static final String IMAGE_PARAMETER_NAME = "file";
    private static final String IMAGE_FILENAME = "image.png";
    private static final String IMAGE_CONTENT_TYPE = "image/png";
    private static final String IMAGE_FILE = "이미지 파일";

    private String accessToken;
    private MockMultipartFile image;
    private ApplicationException memberNotFoundException;
    private ApplicationException wrongFileExtensionException;
    private ApplicationException wrongFileSizeException;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        accessToken = jwtService.createAccessToken(ID);
        image = new MockMultipartFile(IMAGE_PARAMETER_NAME, IMAGE_FILENAME, IMAGE_CONTENT_TYPE, IMAGE_FILE.getBytes());
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        wrongFileExtensionException = new WrongFormException(ApplicationError.INVALID_FILE_EXTENSION);
        wrongFileSizeException = new WrongFormException(ApplicationError.INVALID_FILE_SIZE);
    }

    @WithMockUser
    @Test
    void 회원_이미지_수정_성공(@Value("${cloud.aws.s3.bucket}") String bucket) throws Exception {
        // given
        willReturn("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + IMAGE_FILENAME).given(fileUploadService).save(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_이미지_수정_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(fileUploadService).save(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_회원정보없음_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_이미지_수정_파일형식미지원_실패() throws Exception {
        // given
        willThrow(wrongFileExtensionException).given(fileUploadService).save(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_파일형식미지원_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 회원_이미지_수정_파일크기미지원_실패() throws Exception {
        // given
        willThrow(wrongFileSizeException).given(fileUploadService).save(any(MultipartFile.class));
        // when
        ResultActions resultActions = 회원_이미지_수정_요청();
        // then
        회원_이미지_수정_요청_파일크기미지원_실패(resultActions);
    }

    private ResultActions 회원_이미지_수정_요청() throws Exception {
        return mockMvc.perform(multipart(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/upload")
                .file(image)
                .header(AUTHORIZATION, BEARER + accessToken)
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
}
