package com.cupid.jikting.member.repository;

import com.cupid.jikting.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    private static final String USERNAME = "username123";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "전화번호";
    private static final String WRONG_USERNAME = "잘못된 아이디";
    private static final String WRONG_PASSWORD = "잘못된 비밀번호";
    private static final String WRONG_PHONE = "잘못된 전화번호";

    private static Stream<Arguments> invalidMemberInfos() {
        return Stream.of(
                arguments(WRONG_USERNAME, PASSWORD, PHONE),
                arguments(USERNAME, WRONG_PASSWORD, PHONE),
                arguments(USERNAME, PASSWORD, WRONG_PHONE)
        );
    }

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

    @Test
    void 전화번호로_회원_조회_성공() {
        // given
        Member member = Member.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
        memberRepository.save(member);
        // when & then
        assertDoesNotThrow(() -> memberRepository.findByPhone(PHONE));
    }

    @Test
    void 전화번호로_회원_조회_실패() {
        // given
        Member member = Member.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
        memberRepository.save(member);
        // when
        Optional<Member> memberFoundByPhone = memberRepository.findByPhone(WRONG_PHONE);
        // then
        assertThat(memberFoundByPhone).isEmpty();
    }

    @Test
    void 아이디와_이름과_전화번호로_회원_조회_성공() {
        // given
        Member member = Member.builder()
                .username(USERNAME)
                .name(NAME)
                .phone(PHONE)
                .build();
        memberRepository.save(member);
        // when
        boolean existsByUsernameAndNameAndPhone = memberRepository.existsByUsernameAndNameAndPhone(USERNAME, NAME, PHONE);
        // then
        assertThat(existsByUsernameAndNameAndPhone).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidMemberInfos")
    void 아이디와_이름과_전화번호로_회원_조회_실패(String username, String name, String phone) {
        // given
        Member member = Member.builder()
                .username(USERNAME)
                .name(NAME)
                .phone(PHONE)
                .build();
        memberRepository.save(member);
        // when
        boolean existsByUsernameAndNameAndPhone = memberRepository.existsByUsernameAndNameAndPhone(username, name, phone);
        // then
        assertThat(existsByUsernameAndNameAndPhone).isFalse();
    }
}
