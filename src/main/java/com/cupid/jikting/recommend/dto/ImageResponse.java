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
    private Long memberProfileId;
    private String url;

    public static ImageResponse from(ProfileImage profileImage) {
        return new ImageResponse(
                profileImage.getSequence().name(),
                profileImage.getMemberProfileId(),
                profileImage.getUrl());
    }
}
