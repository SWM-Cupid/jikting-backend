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

    private String birth;
    private int height;
    private String address;
    private String mbti;
    private String smokeStatus;
    private String drinkStatus;
    private String college;
    private String description;
    private List<String> personalities;
    private List<String> hobbies;
    private List<ImageResponse> images;

    public static MemberProfileResponse of(MemberProfile memberProfile) {
        List<ImageResponse> images = memberProfile.getProfileImages()
                .stream()
                .map(ImageResponse::of)
                .collect(Collectors.toList());
        if (memberProfile.isNotFilled()) {
            return new MemberProfileResponse(
                    "", 0, "", "", "", "", "", "",
                    memberProfile.getPersonalityKeywords(), memberProfile.getHobbyKeywords(), images
            );
        }
        return new MemberProfileResponse(
                memberProfile.getBirth().toString(),
                memberProfile.getHeight(),
                memberProfile.getAddress(),
                memberProfile.getMbti().name(),
                memberProfile.getSmokeStatus().getMessage(),
                memberProfile.getDrinkStatus().getMessage(),
                memberProfile.getCollege(),
                memberProfile.getDescription(),
                memberProfile.getPersonalityKeywords(),
                memberProfile.getHobbyKeywords(),
                images
        );
    }
}
