package com.cupid.jikting.recommend.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponse {

    private boolean isMain;
    private Long memberId;
    private String url;
}
