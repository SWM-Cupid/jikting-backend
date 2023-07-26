package com.cupid.jikting.recommend.dto;

import com.cupid.jikting.member.entity.ProfileImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponse {

    private String sequence;
    private Long memberId;
    private String url;

    public static ImageResponse toImageResponse(ProfileImage profileImage) {
        return new ImageResponse(
                profileImage.getSequence().name(),
                profileImage.getMemberProfile().getId(),
                profileImage.getUrl());
    }
}
