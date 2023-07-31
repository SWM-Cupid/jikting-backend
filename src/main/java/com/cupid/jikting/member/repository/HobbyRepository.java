package com.cupid.jikting.member.repository;

import com.cupid.jikting.common.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {

    Optional<Hobby> findByKeyword(String keyword);
}
