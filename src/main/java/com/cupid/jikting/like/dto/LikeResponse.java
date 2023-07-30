package com.cupid.jikting.like.dto;

import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamLike;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponse {

    private Long likeId;
    private String name;
    private List<String> keywords;
    private List<String> imageUrls;

    public static LikeResponse fromReceivedTeamLike(TeamLike teamLike) {
        return from(teamLike, teamLike.getReceivedTeam());
    }

    public static LikeResponse fromSentTeamLike(TeamLike teamLike) {
        return from(teamLike, teamLike.getSentTeam());
    }

    private static LikeResponse from(TeamLike teamLike, Team team) {
        return new LikeResponse(
                teamLike.getId(),
                team.getName(),
                team.getPersonalities(),
                team.getMainImageUrls());
    }
}
