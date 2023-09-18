package com.cupid.jikting.recommend.service;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.recommend.repository.RecommendRepository;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamLike;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.TeamLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RecommendServiceTest {

    private static final Long ID = 1L;
    private static final LocalDate BIRTH = LocalDate.of(1997, 9, 11);
    private static final String ADDRESS = "지역";
    private static final String COMPANY = "회사";
    private static final int HEIGHT = 180;
    private static final String DESCRIPTION = "자기소개";
    private static final String COLLEGE = "대학";
    private static final boolean LEADER = true;

    private MemberProfile memberProfile;
    private Recommend recommend;
    private ApplicationException memberNotFoundException;
    private ApplicationException recommendNotFoundException;

    @InjectMocks
    private RecommendService recommendService;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private RecommendRepository recommendRepository;

    @Mock
    private TeamLikeRepository teamLikeRepository;

    @BeforeEach
    void setUp() {
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        List<MemberCompany> memberCompanies = List.of(MemberCompany.builder()
                .company(company)
                .build());
        Member member = Member.builder()
                .memberCompanies(memberCompanies)
                .build();
        Personality personality = Personality.builder().build();
        List<MemberPersonality> memberPersonalities = List.of(MemberPersonality.builder()
                .personality(personality)
                .build());
        Hobby hobby = Hobby.builder().build();
        List<MemberHobby> memberHobbies = List.of(MemberHobby.builder()
                .hobby(hobby)
                .build());
        Team teamFrom = Team.builder()
                .id(ID)
                .build();
        memberProfile = MemberProfile.builder()
                .birth(BIRTH)
                .mbti(Mbti.ENFJ)
                .address(ADDRESS)
                .member(member)
                .smokeStatus(SmokeStatus.SMOKING)
                .drinkStatus(DrinkStatus.OFTEN)
                .height(HEIGHT)
                .description(DESCRIPTION)
                .college(COLLEGE)
                .build();
        memberProfile.getMemberPersonalities().update(memberPersonalities);
        memberProfile.getMemberHobbies().update(memberHobbies);
        TeamMember.of(LEADER, teamFrom, memberProfile);
        List<Recommend> recommends = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> Recommend.builder()
                        .from(teamFrom)
                        .build())
                .collect(Collectors.toList());
        Team teamTo = Team.builder()
                .recommends(recommends)
                .build();
        TeamMember.of(LEADER, teamTo, memberProfile);
        recommend = Recommend.builder()
                .build();
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
        recommendNotFoundException = new NotFoundException(ApplicationError.RECOMMEND_NOT_FOUND);
    }

    @Test
    void 추천팀_조회_성공() {
        //given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        //when
        List<RecommendResponse> recommendResponses = recommendService.get(ID);
        //then
        assertThat(recommendResponses).hasSize(memberProfile.getRecommends().size());
    }

    @Test
    void 추천팀_조회_실패_회원_프로필_없음() {
        // given
        willThrow(memberNotFoundException).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> recommendService.get(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 호감_보내기_성공() {
        //given
        willReturn(Optional.of(recommend)).given(recommendRepository).findById(anyLong());
        //when
        recommendService.sendLike(ID);
        //then
        assertAll(
                () -> verify(recommendRepository).findById(anyLong()),
                () -> verify(teamLikeRepository).save(any(TeamLike.class))
        );
    }

    @Test
    void 호감_보내기_실패_추천_없음() {
        //given
        willThrow(recommendNotFoundException).given(recommendRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> recommendService.sendLike(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.RECOMMEND_NOT_FOUND.getMessage());
    }

    @Test
    void 호감_넘기기_성공() {
        //given
        willReturn(Optional.of(recommend)).given(recommendRepository).findById(anyLong());
        //when
        recommendService.sendLike(ID);
        //then
        verify(recommendRepository).findById(anyLong());
    }

    @Test
    void 호감_넘기정_실패_추천_없음() {
        //given
        willThrow(recommendNotFoundException).given(recommendRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> recommendService.sendLike(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.RECOMMEND_NOT_FOUND.getMessage());
    }
}
