package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.SignupRequest;
import com.cupid.jikting.member.entity.Gender;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.RollbackException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private static final String USERNAME = "username123";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "01000000000";

    private SignupRequest signupRequest;
    private Member member;
    private RollbackException rollbackException;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .name(NAME)
                .phone(PHONE)
                .build();
        member = Member.builder()
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .gender(Gender.MALE)
                .name(NAME)
                .phone(PHONE)
                .build();
        rollbackException = new RollbackException();
    }

    @Test
    void 회원_가입_성공() {
        // given
        willReturn(member).given(memberRepository).save(any(Member.class));
        // when
        memberService.signup(signupRequest);
        // then
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void 회원_가입_실패() {
        //given
        willThrow(rollbackException).given(memberRepository).save(any(Member.class));
        //when & then
        assertThatThrownBy(() -> memberService.signup(signupRequest))
                .isInstanceOf(RollbackException.class);
    }
}
