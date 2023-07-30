package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.ProfileImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageRequest {

    private Long profileImageId;
    private String url;
    private String sequence;

    public static ImageRequest of(ProfileImage profileImage) {
        return new ImageRequest(profileImage.getUrl(), profileImage.getSequence().name());
    }
}
