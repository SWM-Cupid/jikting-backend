package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.*;
import org.springframework.stereotype.Service;

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

    public void update(MemberUpdateRequest memberUpdateRequest) {
    }

    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
    }
}
