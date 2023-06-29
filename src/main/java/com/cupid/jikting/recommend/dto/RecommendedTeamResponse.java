package com.cupid.jikting.recommend.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedTeamResponse {

    private Long meetingRecommendationId;
    private List<MemberResponse> members;
    private List<String> personalities;
}
