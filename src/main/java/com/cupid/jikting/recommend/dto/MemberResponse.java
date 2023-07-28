package com.cupid.jikting.recommend.dto;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.member.entity.MemberHobby;
import com.cupid.jikting.member.entity.MemberPersonality;
import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String nickname;
    private List<ImageResponse> images;
    private int age;
    private String mbti;
    private String address;
    private String company;
    private String smokeStatus;
    private String drinkStatus;
    private int height;
    private String description;
    private List<String> personalities;
    private List<String> hobbies;
    private String college;

    public static MemberResponse from(MemberProfile memberProfile) {
        return new MemberResponse(
                memberProfile.getNickname(),
                getImageResponses(memberProfile),
                memberProfile.getAge(),
                memberProfile.getMbti().toString(),
                memberProfile.getAddress(),
                memberProfile.getCompany(),
                memberProfile.getSmokeStatus().getValue(),
                memberProfile.getDrinkStatus().getMessage(),
                memberProfile.getHeight(),
                memberProfile.getDescription(),
                getMemberProfilePersonalities(memberProfile),
                getHobbies(memberProfile),
                memberProfile.getCollege());
    }

    private static List<ImageResponse> getImageResponses(MemberProfile memberProfile) {
        return memberProfile.getProfileImages()
                .stream()
                .map(ImageResponse::from)
                .collect(Collectors.toList());
    }

    private static List<String> getMemberProfilePersonalities(MemberProfile memberProfile) {
        return memberProfile.getMemberPersonalities()
                .stream()
                .map(MemberPersonality::getPersonality)
                .map(Personality::getKeyword)
                .collect(Collectors.toList());
    }

    private static List<String> getHobbies(MemberProfile memberProfile) {
        return memberProfile.getMemberHobbies()
                .stream()
                .map(MemberHobby::getHobby)
                .map(Hobby::getKeyword)
                .collect(Collectors.toList());
    }
}
