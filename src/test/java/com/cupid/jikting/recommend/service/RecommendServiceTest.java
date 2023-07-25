package com.cupid.jikting.recommend.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

@WebMvcTest(MockitoExtension.class)
public class RecommendServiceTest {

    private static final Long ID = 1L;
    private static final String TEAM_NAME = "팀 이름";

    private MemberProfile memberProfile;
    private ApplicationException memberNotFoundException;

    @InjectMocks
    private RecommendService recommendService;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @BeforeEach
    void setUp() {
        Team team = Team.builder()
                .id(ID)
                .name(TEAM_NAME)
                .build();
        List<TeamMember> teamMembers = List.of(TeamMember.builder()
                .id(ID)
                .memberProfile(memberProfile)
                .team(team)
                .build());
        memberProfile = MemberProfile.builder()
                .id(ID)
                .teamMembers(teamMembers)
                .build();
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
    }

    @Test
    void 추천팀_조회_성공() {
        //given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        //when
        List<RecommendResponse> recommendResponses = recommendService.get(ID);
        //then
        assertThat(recommendResponses.size()).isEqualTo(3);
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
}
