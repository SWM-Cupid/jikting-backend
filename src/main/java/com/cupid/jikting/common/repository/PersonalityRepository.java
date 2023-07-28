package com.cupid.jikting.common.repository;

import com.cupid.jikting.common.entity.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {

    Optional<Personality> findByKeyword(String keyword);
}
