package com.cupid.jikting.team.repository;

import com.cupid.jikting.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);
}
