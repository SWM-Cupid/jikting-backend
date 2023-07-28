package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.MemberProfile;
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

    public static MemberResponse of(MemberProfile memberProfile) {
        return new MemberResponse(
                memberProfile.getNickname(),
                memberProfile.getCompany(),
                memberProfile.getMainImageUrl());
    }
}
