package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.Team;

public interface CustomTeamRepository {

    Team findRecommendingTeamFor(Team team);
}
