package com.cupid.jikting.recommend.dto;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.team.entity.TeamPersonality;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendResponse {

    private Long recommendId;
    private String name;
    private String description;
    private List<String> personalities;
    private List<MemberResponse> members;

    public static RecommendResponse from(Recommend recommend) {
        return new RecommendResponse(
                recommend.getId(),
                recommend.getFromTeamName(),
                recommend.getFromTeamDescription(),
                getTeamPersonalities(recommend),
                getMemberResponses(recommend));
    }

    private static List<MemberResponse> getMemberResponses(Recommend recommend) {
        return recommend.getFrom()
                .getMemberProfiles()
                .stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    private static List<String> getTeamPersonalities(Recommend recommend) {
        return recommend.getFromTeamPersonalities()
                .stream()
                .map(TeamPersonality::getPersonality)
                .map(Personality::getKeyword)
                .collect(Collectors.toList());
    }
}
