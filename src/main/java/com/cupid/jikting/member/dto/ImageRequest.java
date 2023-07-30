package com.cupid.jikting.member.dto;

import com.cupid.jikting.member.entity.ProfileImage;
import com.cupid.jikting.member.entity.Sequence;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageRequest {

    private Long profileImageId;
    private String url;
    private String sequence;

    public ProfileImage toProfileImage() {
        return ProfileImage.builder()
                .id(profileImageId)
                .url(url)
                .sequence(Sequence.valueOf(sequence))
                .build();
    }
}
