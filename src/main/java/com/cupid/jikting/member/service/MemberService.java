package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {

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

    public void createVerificationCodeForResetPassword(PasswordResetRequest passwordResetRequest) {
    }
}
