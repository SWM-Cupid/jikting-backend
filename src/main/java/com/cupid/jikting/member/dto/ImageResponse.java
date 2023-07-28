package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.ProfileImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponse {

    private String url;
    private String sequence;

    public static ImageResponse of(ProfileImage profileImage) {
        return new ImageResponse(profileImage.getUrl(), profileImage.getSequence().name());
    }
}
