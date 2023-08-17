package com.cupid.jikting.chatting.dto;

import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long memberProfileId;
    private String nickname;
    private String image;

    public static MemberResponse from(MemberProfile memberProfile) {
        return new MemberResponse(memberProfile.getId(), memberProfile.getNickname(), memberProfile.getMainImageUrl());
    }
}
