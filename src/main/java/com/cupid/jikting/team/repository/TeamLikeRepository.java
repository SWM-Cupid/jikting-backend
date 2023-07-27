package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.TeamLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamLikeRepository extends JpaRepository<TeamLike, Long> {

    Optional<TeamLike> getTeamLikesByReceivedTeamId(Long teamId);
}
