package com.cupid.jikting.recommend.dto;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.team.entity.TeamPersonality;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendResponse {

    private Long recommendId;
    private List<MemberResponse> members;
    private List<String> personalities;

    public static RecommendResponse from(Recommend recommend) {
        return new RecommendResponse(
                recommend.getId(),
                getMemberResponses(recommend),
                getTeamPersonalities(recommend));
    }

    private static List<MemberResponse> getMemberResponses(Recommend recommend) {
        return recommend.getFrom()
                .getMemberProfiles()
                .stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    private static List<String> getTeamPersonalities(Recommend recommend) {
        return recommend.getFrom()
                .getTeamPersonalities()
                .stream()
                .map(TeamPersonality::getPersonality)
                .map(Personality::getKeyword)
                .collect(Collectors.toList());
    }
}
