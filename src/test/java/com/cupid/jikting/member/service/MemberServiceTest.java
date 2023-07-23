package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.member.dto.UsernameCheckRequest;
import com.cupid.jikting.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String USERNAME = "아이디";

    private UsernameCheckRequest usernameCheckRequest;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        usernameCheckRequest = UsernameCheckRequest.builder()
                .username(USERNAME)
                .build();
    }

    @Test
    void 아이디_중복_확인_성공() {
        // given
        willReturn(false).given(memberRepository).existsByUsername(anyString());
        // when
        memberService.checkDuplicatedUsername(usernameCheckRequest);
        // then
        verify(memberRepository).existsByUsername(anyString());
    }

    @Test
    void 아이디_중복_확인_실패_존재하는_아이디() {
        // given
        willReturn(true).given(memberRepository).existsByUsername(anyString());
        // when & then
        assertThatThrownBy(() -> memberService.checkDuplicatedUsername(usernameCheckRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ApplicationError.DUPLICATE_USERNAME.getMessage());
    }
}
