package com.cupid.jikting.team.dto;

import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {

    private String nickname;
    private String image;
    private int age;
    private String mbti;
    private String address;
    private String type;
    private int height;
    private String description;
    private String company;
    private String smokeStatus;
    private String drinkStatus;

    public static MemberProfileResponse from(MemberProfile memberProfile) {
        return new MemberProfileResponse(
                memberProfile.getNickname(),
                memberProfile.getMainImageUrl(),
                memberProfile.getAge(),
                memberProfile.getMbti().toString(),
                memberProfile.getAddress(),
                memberProfile.getType(),
                memberProfile.getHeight(),
                memberProfile.getDescription(),
                memberProfile.getCompany(),
                memberProfile.getSmokeStatus().getMessage(),
                memberProfile.getDrinkStatus().getMessage());
    }
}
