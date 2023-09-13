package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsernameResponse {

    private String username;

    public static UsernameResponse from(Member member) {
        return new UsernameResponse(member.getUsername());
    }
}
