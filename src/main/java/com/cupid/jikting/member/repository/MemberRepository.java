package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNameAndPhone(String name, String phone);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<Member> findByPhone(String phone);

    boolean existsByUsernameAndNameAndPhone(String username, String name, String phone);
}
