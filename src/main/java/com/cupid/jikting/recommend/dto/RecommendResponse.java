package com.cupid.jikting.recommend.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendResponse {

    private Long recommendId;
    private List<MemberResponse> members;
    private List<String> personalities;
}
