package com.cupid.jikting.like.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponse {

    private Long likeId;
    private String name;
    private List<String> keywords;
    private List<String> imageUrls;
}
