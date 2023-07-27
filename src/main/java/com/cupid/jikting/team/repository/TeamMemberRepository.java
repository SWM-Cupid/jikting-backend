package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<Long> getTeamIdByMemberProfileId(Long memberProfileId);
}
