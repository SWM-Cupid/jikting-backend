package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.MemberProfileResponse;
import com.cupid.jikting.member.dto.MemberResponse;
import com.cupid.jikting.member.dto.MemberUpdateRequest;
import com.cupid.jikting.member.dto.SignupRequest;
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
}
