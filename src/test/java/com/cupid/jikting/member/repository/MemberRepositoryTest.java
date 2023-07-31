package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    private static final String USERNAME = "username123";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "01000000000";

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원_삭제_성공() {
        // given
        Member member = Member.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
        Member savedMember = memberRepository.save(member);
        // when
        memberRepository.delete(member);
        // then
        assertThat(memberRepository.findById(savedMember.getId())).isEmpty();
    }
}
