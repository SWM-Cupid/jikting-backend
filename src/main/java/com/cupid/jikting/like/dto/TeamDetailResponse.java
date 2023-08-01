package com.cupid.jikting.like.dto;

import com.cupid.jikting.team.entity.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDetailResponse {

    private Long likeId;
    private String teamName;
    private List<String> keywords;
    private List<MemberProfileResponse> members;

    public static TeamDetailResponse of(Long likeId, Team team) {
        List<MemberProfileResponse> memberProfileResponses = team.getMemberProfiles()
                .stream()
                .map(MemberProfileResponse::from)
                .collect(Collectors.toList());
        return new TeamDetailResponse(
                likeId,
                team.getName(),
                team.getPersonalities(),
                memberProfileResponses);
    }
}
