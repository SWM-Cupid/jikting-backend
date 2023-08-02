package com.cupid.jikting.like.dto;

import com.cupid.jikting.member.entity.MemberProfile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {

    private String nickname;
    private String image;
    private int age;
    private String mbti;
    private String address;
    private String company;
    private String smokeStatus;
    private String drinkStatus;
    private int height;
    private String description;
    private List<String> keywords;
    private String college;

    public static MemberProfileResponse from(MemberProfile memberProfile) {
        List<String> keywords = new ArrayList<>();
        keywords.addAll(memberProfile.getPersonalityKeywords());
        keywords.addAll(memberProfile.getHobbyKeywords());
        return new MemberProfileResponse(
                memberProfile.getNickname(),
                memberProfile.getMainImageUrl(),
                memberProfile.getAge(),
                memberProfile.getMbti().toString(),
                memberProfile.getAddress(),
                memberProfile.getCompany(),
                memberProfile.getSmokeStatus().toString(),
                memberProfile.getDrinkStatus().toString(),
                memberProfile.getHeight(),
                memberProfile.getDescription(),
                keywords,
                memberProfile.getCollege());
    }
}
