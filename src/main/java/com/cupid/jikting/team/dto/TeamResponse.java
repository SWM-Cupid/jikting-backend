package com.cupid.jikting.team.dto;

import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamResponse {

    private String name;
    private String description;
    private List<String> keywords;
    private List<MemberProfileResponse> members;

    public static TeamResponse from(Team team) {
        List<MemberProfileResponse> memberProfileResponses = team.getTeamMembers()
                .stream()
                .map(TeamMember::getMemberProfile)
                .map(MemberProfileResponse::from)
                .collect(Collectors.toList());
        return new TeamResponse(
                team.getName(),
                team.getDescription(),
                team.getPersonalities(),
                memberProfileResponses);
    }
}
