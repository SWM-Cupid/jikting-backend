package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.dto.MemberResponse;
import com.cupid.jikting.member.dto.NicknameCheckRequest;
import com.cupid.jikting.member.dto.SignupRequest;
import com.cupid.jikting.member.dto.UsernameCheckRequest;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.RollbackException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private static final String USERNAME = "username123";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "01000000000";
    private static final String NICKNAME = "닉네임";
    private static final String COMPANY = "회사";
    private static final String IMAGE_URL = "이미지 URL";
    private static final Long ID = 1L;

    private Member member;
    private MemberProfile memberProfile;
    private SignupRequest signupRequest;
    private UsernameCheckRequest usernameCheckRequest;
    private NicknameCheckRequest nicknameCheckRequest;
    private RollbackException rollbackException;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        ProfileImage profileImage = ProfileImage.builder()
                .sequence(Sequence.MAIN)
                .url(IMAGE_URL)
                .build();
        MemberCompany memberCompany = MemberCompany.builder()
                .member(member)
                .company(company)
                .build();
        member = Member.builder()
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .gender(Gender.MALE).name(NAME)
                .phone(PHONE)
                .build();
        memberProfile = MemberProfile.builder()
                .member(member)
                .nickname(NICKNAME)
                .build();
        memberProfile.getMember().getMemberCompanies().add(memberCompany);
        memberProfile.getProfileImages().add(profileImage);
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .gender(Gender.MALE.getKey())
                .name(NAME)
                .phone(PHONE)
                .build();
        usernameCheckRequest = UsernameCheckRequest.builder()
                .username(USERNAME)
                .build();
        nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname(NICKNAME)
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

    @Test
    void 회원_조회_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        // when
        MemberResponse memberResponse = memberService.get(ID);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> assertThat(memberResponse.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(memberResponse.getCompany()).isEqualTo(COMPANY),
                () -> assertThat(memberResponse.getImageUrl()).isEqualTo(IMAGE_URL)
        );
    }

    @Test
    void 회원_조회_실패_회원_없음() {
        //given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> memberService.get(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
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
        Assertions.assertThatThrownBy(() -> memberService.checkDuplicatedUsername(usernameCheckRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ApplicationError.DUPLICATE_USERNAME.getMessage());
    }

    @Test
    void 닉네임_중복_확인_성공() {
        // given
        willReturn(false).given(memberProfileRepository).existsByNickname(anyString());
        // when
        memberService.checkDuplicatedNickname(nicknameCheckRequest);
        // then
        verify(memberProfileRepository).existsByNickname(anyString());
    }

    @Test
    void 닉네임_중복_확인_실패_존재하는_닉네임() {
        // given
        willReturn(true).given(memberProfileRepository).existsByNickname(anyString());
        // when & then
        Assertions.assertThatThrownBy(() -> memberService.checkDuplicatedNickname(nicknameCheckRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ApplicationError.DUPLICATE_NICKNAME.getMessage());
    }
}
