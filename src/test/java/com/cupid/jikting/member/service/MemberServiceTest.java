package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.member.dto.NicknameCheckRequest;
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
    private static final String NICKNAME = "닉네임";

    private UsernameCheckRequest usernameCheckRequest;
    private NicknameCheckRequest nicknameCheckRequest;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        usernameCheckRequest = UsernameCheckRequest.builder()
                .username(USERNAME)
                .build();
        nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname(NICKNAME)
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

    @Test
    void 닉네임_중복_확인_성공() {
        // given
        willReturn(false).given(memberRepository).existsByNickname(anyString());
        // when
        memberService.checkDuplicatedNickname(nicknameCheckRequest);
        // then
        verify(memberRepository).existsByNickname(anyString());
    }

    @Test
    void 닉네임_중복_확인_실패_존재하는_닉네임() {
        // given
        willReturn(true).given(memberRepository).existsByNickname(anyString());
        // when & then
        assertThatThrownBy(() -> memberService.checkDuplicatedNickname(nicknameCheckRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ApplicationError.DUPLICATE_NICKNAME.getMessage());
    }
}
