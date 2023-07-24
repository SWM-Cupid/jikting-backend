package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(SignupRequest signupRequest) {
    }

    public MemberResponse get(Long memberId) {
        return null;
    }

    public MemberProfileResponse getProfile(Long memberId) {
        return null;
    }

    public void update(NicknameUpdateRequest nicknameUpdateRequest) {
    }

    public void updateProfile(MemberProfileUpdateRequest memberProfileUpdateRequest) {
    }

    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
    }

    public void updateImage(MultipartFile multipartFile) {
    }

    public void withdraw(Long memberId, WithdrawRequest withdrawRequest) {
    }

    public void checkDuplicatedUsername(UsernameCheckRequest usernameCheckRequest) {
        if (memberRepository.existsByUsername(usernameCheckRequest.getUsername())) {
            throw new DuplicateException(ApplicationError.DUPLICATE_USERNAME);
        }
    }

    public void checkDuplicatedNickname(NicknameCheckRequest nicknameCheckRequest) {
        if (memberRepository.existsByNickname(nicknameCheckRequest.getNickname())) {
            throw new DuplicateException(ApplicationError.DUPLICATE_NICKNAME);
        }
    }

    public void createVerificationCodeForSignup(SignUpVerificationCodeRequest signUpVerificationCodeRequest) {
    }

    public void verifyPhoneForSignup(VerificationRequest verificationRequest) {
    }

    public void createVerificationCodeForSearchUsername(UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest) {
    }

    public UsernameResponse verifyForSearchUsername(VerificationRequest verificationRequest) {
        return null;
    }

    public void createVerificationCodeForResetPassword(PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest) {
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
}
