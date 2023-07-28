package com.cupid.jikting.team.dto;

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
    private String image;
    private int age;
    private String mbti;
    private String address;

    public static MemberResponse from(MemberProfile memberProfile) {
        return new MemberResponse(
                memberProfile.getNickname(),
                memberProfile.getMainImageUrl(),
                memberProfile.getAge(),
                memberProfile.getMbti().toString(),
                memberProfile.getAddress());
    }
}
