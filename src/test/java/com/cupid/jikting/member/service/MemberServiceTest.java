package com.cupid.jikting.member.service;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.DuplicateException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.error.UnAuthorizedException;
import com.cupid.jikting.common.repository.PersonalityRepository;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.repository.HobbyRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private static final String USERNAME = "username123";
    private static final String PASSWORD = "Password123!";
    private static final String NAME = "홍길동";
    private static final String PHONE = "01000000000";
    private static final Long ID = 1L;
    private static final String IMAGE_URL = "사진 URL";
    private static final String NICKNAME = "닉네임";
    private static final String COMPANY = "회사";
    private static final LocalDate BIRTH = LocalDate.of(1996, 5, 10);
    private static final int HEIGHT = 168;
    private static final String ADDRESS = "거주지";
    private static final String COLLEGE = "출신학교(선택사항 - 없을 시 빈 문자열)";
    private static final String PERSONALITY = "성격";
    private static final String HOBBY = "취미";
    private static final String DESCRIPTION = "한줄 소개(선택사항 - 없을 시 빈 문자열)";

    private Member member;
    private MemberProfile memberProfile;
    private Personality personality;
    private Hobby hobby;
    private List<ProfileImage> profileImages;
    private List<MemberPersonality> memberPersonalities;
    private List<MemberHobby> memberHobbies;
    private SignupRequest signupRequest;
    private UsernameCheckRequest usernameCheckRequest;
    private NicknameCheckRequest nicknameCheckRequest;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private PersonalityRepository personalityRepository;

    @Mock
    private HobbyRepository hobbyRepository;

    @BeforeEach
    void setUp() {
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        List<MemberCompany> memberCompanies = IntStream.range(0, 3)
                .mapToObj(n -> MemberCompany.builder()
                        .member(member)
                        .company(company)
                        .build())
                .collect(Collectors.toList());
        member = Member.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .name(NAME)
                .phone(PHONE)
                .memberCompanies(memberCompanies)
                .build();
        profileImages = IntStream.range(0, 1)
                .mapToObj(n -> ProfileImage.builder()
                        .id(ID)
                        .url(IMAGE_URL)
                        .sequence(Sequence.MAIN)
                        .build())
                .collect(Collectors.toList());
        personality = Personality.builder()
                .keyword(PERSONALITY)
                .build();
        hobby = Hobby.builder()
                .keyword(HOBBY)
                .build();
        memberPersonalities = IntStream.range(0, 1)
                .mapToObj(n -> MemberPersonality.builder()
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
        memberHobbies = IntStream.range(0, 1)
                .mapToObj(n -> MemberHobby.builder()
                        .hobby(hobby)
                        .build())
                .collect(Collectors.toList());
        memberProfile = MemberProfile.builder()
                .nickname(NICKNAME)
                .member(member)
                .build();
        memberProfile.updateProfile(BIRTH, HEIGHT, Mbti.ENFJ, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                memberPersonalities, memberHobbies, profileImages);
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .nickname(NICKNAME)
                .gender(Gender.MALE.getMessage())
                .build();
        usernameCheckRequest = UsernameCheckRequest.builder()
                .username(USERNAME)
                .build();
        nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname(NICKNAME)
                .build();
    }

    @Test
    void 회원_가입_성공() {
        // given
        willReturn(PASSWORD).given(passwordEncoder).encode(anyString());
        willReturn(member).given(memberRepository).save(any(Member.class));
        // when
        memberService.signup(signupRequest);
        // then
        assertAll(
                () -> verify(passwordEncoder).encode(anyString()),
                () -> verify(memberRepository).save(any(Member.class))
        );
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
    void 회원_프로필_조회_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        // when
        MemberProfileResponse memberProfileResponse = memberService.getProfile(ID);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> assertThat(memberProfileResponse.getImages().size()).isEqualTo(profileImages.size()),
                () -> assertThat(memberProfileResponse.getAge()).isEqualTo(memberProfile.getAge()),
                () -> assertThat(memberProfileResponse.getHeight()).isEqualTo(memberProfile.getHeight()),
                () -> assertThat(memberProfileResponse.getAddress()).isEqualTo(memberProfile.getAddress()),
                () -> assertThat(memberProfileResponse.getMbti()).isEqualTo(memberProfile.getMbti().name()),
                () -> assertThat(memberProfileResponse.getSmokeStatus()).isEqualTo(memberProfile.getSmokeStatus().getMessage()),
                () -> assertThat(memberProfileResponse.getDrinkStatus()).isEqualTo(memberProfile.getDrinkStatus().getMessage()),
                () -> assertThat(memberProfileResponse.getCollege()).isEqualTo(memberProfile.getCollege()),
                () -> assertThat(memberProfileResponse.getPersonalities().size()).isEqualTo(memberPersonalities.size()),
                () -> assertThat(memberProfileResponse.getHobbies().size()).isEqualTo(memberHobbies.size()),
                () -> assertThat(memberProfileResponse.getDescription()).isEqualTo(memberProfile.getDescription())
        );
    }

    @Test
    void 회원_프로필_조회_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> memberService.getProfile(ID))
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

    @Test
    void 회원_닉네임_수정_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        NicknameUpdateRequest nicknameUpdateRequest = NicknameUpdateRequest.builder()
                .nickname(NICKNAME)
                .build();
        // when
        memberService.update(ID, nicknameUpdateRequest);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(memberProfileRepository).save(any(MemberProfile.class))
        );
    }

    @Test
    void 회원_프로필_수정_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(personality)).given(personalityRepository).findByKeyword(anyString());
        willReturn(Optional.of(hobby)).given(hobbyRepository).findByKeyword(anyString());
        MemberProfileUpdateRequest memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .images(profileImages.stream()
                        .map(profileImage -> ImageRequest.builder()
                                .profileImageId(profileImage.getId())
                                .url(profileImage.getUrl())
                                .sequence(profileImage.getSequence().name())
                                .build())
                        .collect(Collectors.toList()))
                .birth(BIRTH)
                .height(HEIGHT)
                .gender(Gender.MALE.getMessage())
                .address(ADDRESS)
                .mbti(Mbti.ENFJ.name())
                .drinkStatus(DrinkStatus.OFTEN.getMessage())
                .smokeStatus(SmokeStatus.SMOKING.getMessage())
                .college(COLLEGE)
                .personalities(memberPersonalities.stream()
                        .map(MemberPersonality::getPersonality)
                        .map(Personality::getKeyword)
                        .collect(Collectors.toList()))
                .hobbies(memberHobbies.stream()
                        .map(MemberHobby::getHobby)
                        .map(Hobby::getKeyword)
                        .collect(Collectors.toList()))
                .description(DESCRIPTION)
                .build();
        // when
        memberService.updateProfile(ID, memberProfileUpdateRequest);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(personalityRepository).findByKeyword(anyString()),
                () -> verify(hobbyRepository).findByKeyword(anyString())
        );
    }

    @Test
    void 회원_프로필_수정_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        MemberProfileUpdateRequest memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .images(profileImages.stream()
                        .map(profileImage -> ImageRequest.builder()
                                .profileImageId(profileImage.getId())
                                .url(profileImage.getUrl())
                                .sequence(profileImage.getSequence().name())
                                .build())
                        .collect(Collectors.toList()))
                .birth(BIRTH)
                .height(HEIGHT)
                .gender(Gender.MALE.getMessage())
                .address(ADDRESS)
                .mbti(Mbti.ENFJ.name())
                .drinkStatus(DrinkStatus.OFTEN.getMessage())
                .smokeStatus(SmokeStatus.SMOKING.getMessage())
                .college(COLLEGE)
                .personalities(memberPersonalities.stream()
                        .map(MemberPersonality::getPersonality)
                        .map(Personality::getKeyword)
                        .collect(Collectors.toList()))
                .hobbies(memberHobbies.stream()
                        .map(MemberHobby::getHobby)
                        .map(Hobby::getKeyword)
                        .collect(Collectors.toList()))
                .description(DESCRIPTION)
                .build();
        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(ID, memberProfileUpdateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 회원_프로필_수정_실패_성격_키워드_없음() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willThrow(new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND)).given(personalityRepository).findByKeyword(anyString());
        MemberProfileUpdateRequest memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .images(profileImages.stream()
                        .map(profileImage -> ImageRequest.builder()
                                .profileImageId(profileImage.getId())
                                .url(profileImage.getUrl())
                                .sequence(profileImage.getSequence().name())
                                .build())
                        .collect(Collectors.toList()))
                .birth(BIRTH)
                .height(HEIGHT)
                .gender(Gender.MALE.getMessage())
                .address(ADDRESS)
                .mbti(Mbti.ENFJ.name())
                .drinkStatus(DrinkStatus.OFTEN.getMessage())
                .smokeStatus(SmokeStatus.SMOKING.getMessage())
                .college(COLLEGE)
                .personalities(memberPersonalities.stream()
                        .map(MemberPersonality::getPersonality)
                        .map(Personality::getKeyword)
                        .collect(Collectors.toList()))
                .hobbies(memberHobbies.stream()
                        .map(MemberHobby::getHobby)
                        .map(Hobby::getKeyword)
                        .collect(Collectors.toList()))
                .description(DESCRIPTION)
                .build();
        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(ID, memberProfileUpdateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.PERSONALITY_NOT_FOUND.getMessage());
    }

    @Test
    void 회원_프로필_수정_실패_취미_키워드_없음() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(personality)).given(personalityRepository).findByKeyword(anyString());
        willThrow(new NotFoundException(ApplicationError.HOBBY_NOT_FOUND)).given(hobbyRepository).findByKeyword(anyString());
        MemberProfileUpdateRequest memberProfileUpdateRequest = MemberProfileUpdateRequest.builder()
                .images(profileImages.stream()
                        .map(profileImage -> ImageRequest.builder()
                                .profileImageId(profileImage.getId())
                                .url(profileImage.getUrl())
                                .sequence(profileImage.getSequence().name())
                                .build())
                        .collect(Collectors.toList()))
                .birth(BIRTH)
                .height(HEIGHT)
                .gender(Gender.MALE.getMessage())
                .address(ADDRESS)
                .mbti(Mbti.ENFJ.name())
                .drinkStatus(DrinkStatus.OFTEN.getMessage())
                .smokeStatus(SmokeStatus.SMOKING.getMessage())
                .college(COLLEGE)
                .personalities(memberPersonalities.stream()
                        .map(MemberPersonality::getPersonality)
                        .map(Personality::getKeyword)
                        .collect(Collectors.toList()))
                .hobbies(memberHobbies.stream()
                        .map(MemberHobby::getHobby)
                        .map(Hobby::getKeyword)
                        .collect(Collectors.toList()))
                .description(DESCRIPTION)
                .build();
        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(ID, memberProfileUpdateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.HOBBY_NOT_FOUND.getMessage());
    }

    @Test
    void 회원_탈퇴_성공() {
        // given
        willReturn(true).given(passwordEncoder).matches(anyString(), anyString());
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willDoNothing().given(memberRepository).delete(member);
        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .password(PASSWORD)
                .build();
        // when
        memberService.withdraw(ID, withdrawRequest);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(memberRepository).delete(any(Member.class))
        );
    }

    @Test
    void 회원_탈퇴_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .password(PASSWORD)
                .build();
        // when & then
        assertThatThrownBy(() -> memberService.withdraw(ID, withdrawRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 회원_탈퇴_실패_비밀번호_불일치() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willThrow(new UnAuthorizedException(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD)).given(passwordEncoder).matches(anyString(), anyString());
        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .password(PASSWORD)
                .build();
        // when & then
        assertThatThrownBy(() -> memberService.withdraw(ID, withdrawRequest))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD.getMessage());
    }
}
