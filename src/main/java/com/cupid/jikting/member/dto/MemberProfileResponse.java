package com.cupid.jikting.member.dto;

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
public class MemberProfileResponse {

    private List<ImageResponse> images;
    private int age;
    private int height;
    private String gender;
    private String address;
    private String mbti;
    private String smokeStatus;
    private String drinkStatus;
    private String college;
    private List<String> personalities;
    private List<String> hobbies;
    private String description;

    public static MemberProfileResponse of(MemberProfile memberProfile) {
        List<ImageResponse> images = memberProfile.getProfileImages()
                .stream()
                .map(ImageResponse::of)
                .collect(Collectors.toList());
        return new MemberProfileResponse(
                images,
                memberProfile.getAge(),
                memberProfile.getHeight(),
                memberProfile.getGender().getKey(),
                memberProfile.getAddress(),
                memberProfile.getMbti().name(),
                memberProfile.getSmokeStatus().getMessage(),
                memberProfile.getDrinkStatus().getMessage(),
                memberProfile.getCollege(),
                memberProfile.getPersonalities(),
                memberProfile.getHobbies(),
                memberProfile.getDescription());
    }
}
