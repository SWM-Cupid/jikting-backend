package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    private Long memberProfileId;
    private String role;
    private String socialType;

    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getMemberProfileId(), member.getRole().getKey(), member.getSocialType().name());
    }
}
