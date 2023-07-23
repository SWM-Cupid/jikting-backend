package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String email);

    Optional<Member> findByRefreshToken(String refreshToken);

    boolean existsByUsername(String username);
}
