package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    boolean existsByNickname(String nickname);
}
