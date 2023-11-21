package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String nickname;
    private String company;
    private String imageUrl;

    public static MemberResponse of(Member member) {
        if (member.isNotCertifiedCompany()) {
            return new MemberResponse(member.getNickname(), "", member.getMainImageUrl());
        }
        return new MemberResponse(member.getNickname(), member.getCompany(), member.getMainImageUrl());
    }
}
