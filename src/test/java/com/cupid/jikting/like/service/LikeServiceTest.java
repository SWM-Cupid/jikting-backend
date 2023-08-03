package com.cupid.jikting.like.service;

import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.team.entity.*;
import com.cupid.jikting.team.repository.TeamLikeRepository;
import com.cupid.jikting.team.repository.TeamMemberRepository;
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
class LikeServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "팀이름";
    private static final String KEYWORD = "키워드";
    private static final String URL = "url";
    private static final int YEAR = 1967;
    private static final int MONTH = 5;
    private static final int DATE = 10;
    private static final int HEIGHT = 189;
    private static final String COMPANY = "회사";
    private static final String ADDRESS = "주소";
    private static final String COLLEGE = "대학";
    private static final String DESCRIPTION = "한 줄 소개";

    private TeamMember teamMember;
    private List<TeamLike> teamLikes;
    private TeamLike teamLike;

    @InjectMocks
    private LikeService likeService;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamLikeRepository teamLikeRepository;

    @Mock
    private ChattingRoomRepository chattingRoomRepository;

    @BeforeEach
    void setUp() {
        Personality personality = Personality.builder()
                .keyword(KEYWORD)
                .build();
        Hobby hobby = Hobby.builder()
                .keyword(KEYWORD)
                .build();
        TeamPersonality teamPersonality = TeamPersonality.builder()
                .personality(personality)
                .build();
        MemberPersonality memberPersonality = MemberPersonality.builder()
                .personality(personality)
                .build();
        MemberHobby memberHobby = MemberHobby.builder()
                .hobby(hobby)
                .build();
        List<ProfileImage> profileImages = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> ProfileImage.builder()
                        .id(ID)
                        .url(URL)
                        .sequence(Sequence.MAIN)
                        .build())
                .collect(Collectors.toList());
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        MemberCompany memberCompany = MemberCompany.builder()
                .company(company)
                .build();
        Member member = Member.builder()
                .memberCompanies(List.of(memberCompany))
                .build();
        MemberProfile memberProfile = MemberProfile.builder()
                .member(member)
                .build();
        memberProfile.updateProfile(LocalDate.of(YEAR, MONTH, DATE), HEIGHT, Mbti.ENFJ, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                List.of(memberPersonality), List.of(memberHobby), profileImages);
        List<TeamMember> teamMembers = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> TeamMember.builder()
                        .memberProfile(memberProfile)
                        .build())
                .collect(Collectors.toList());
        Team sentTeam = Team.builder()
                .name(NAME)
                .teamMembers(teamMembers)
                .build();
        sentTeam.addTeamPersonalities(List.of(teamPersonality));
        Team receivedTeam = Team.builder()
                .name(NAME)
                .teamMembers(teamMembers)
                .build();
        receivedTeam.addTeamPersonalities(List.of(teamPersonality));
        teamLike = TeamLike.builder()
                .id(ID)
                .sentTeam(sentTeam)
                .receivedTeam(receivedTeam)
                .acceptStatus(AcceptStatus.INITIAL)
                .build();
        Team team = Team.builder()
                .id(ID)
                .receivedTeamLikes(List.of(teamLike))
                .sentTeamLikes(List.of(teamLike))
                .build();
        teamMember = TeamMember.builder()
                .team(team)
                .build();
        teamLikes = List.of(TeamLike.builder()
                .sentTeam(team)
                .build());
    }

    @Test
    void 받은_호감_조회_성공() {
        // given
        willReturn(Optional.of(teamMember)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when
        List<LikeResponse> likeResponses = likeService.getAllReceivedLike(ID);
        // then
        assertAll(
                () -> verify(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong()),
                () -> assertThat(likeResponses.size()).isEqualTo(teamLikes.size())
        );
    }

    @Test
    void 받은_호감_조회_실패_팀_없음() {
        // given
        willThrow(new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when & then
        assertThatThrownBy(() -> likeService.getAllReceivedLike(ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ApplicationError.NOT_EXIST_REGISTERED_TEAM.getMessage());
    }

    @Test
    void 보낸_호감_조회_성공() {
        // given
        willReturn(Optional.of(teamMember)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when
        List<LikeResponse> likeResponses = likeService.getAllSentLike(ID);
        // then
        assertAll(
                () -> verify(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong()),
                () -> assertThat(likeResponses.size()).isEqualTo(teamLikes.size())
        );
    }

    @Test
    void 보낸_호감_조회_실패_팀_없음() {
        // given
        willThrow(new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when & then
        assertThatThrownBy(() -> likeService.getAllSentLike(ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ApplicationError.NOT_EXIST_REGISTERED_TEAM.getMessage());
    }

    @Test
    void 받은_호감_수락_성공() {
        //given
        willReturn(Optional.of(teamLike)).given(teamLikeRepository).findById(anyLong());
        //when
        likeService.acceptLike(ID);
        //then
        assertAll(
                () -> verify(teamLikeRepository).findById(anyLong()),
                () -> verify(teamLikeRepository).save(any(TeamLike.class)),
                () -> verify(chattingRoomRepository).save(any(ChattingRoom.class))
        );
    }

    @Test
    void 받은_호감_수락_실패_호감_없음() {
        //given
        willThrow(new NotFoundException(ApplicationError.LIKE_NOT_FOUND)).given(teamLikeRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> likeService.acceptLike(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.LIKE_NOT_FOUND.getMessage());
    }

    @Test
    void 받은_호감_거절_성공() {
        //given
        willReturn(Optional.of(teamLike)).given(teamLikeRepository).findById(anyLong());
        //when
        likeService.rejectLike(ID);
        //then
        verify(teamLikeRepository).findById(anyLong());
    }

    @Test
    void 받은_호감_거절_실패_호감_없음() {
        //given
        willThrow(new NotFoundException(ApplicationError.LIKE_NOT_FOUND)).given(teamLikeRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> likeService.rejectLike(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.LIKE_NOT_FOUND.getMessage());
    }

    @Test
    void 보낸팀_상세_조회() {
        //given
        willReturn(Optional.of(teamLike)).given(teamLikeRepository).findById(anyLong());
        //when
        TeamDetailResponse teamDetailResponse = likeService.getSentTeamDetail(ID);
        //then
        assertAll(
                () -> verify(teamLikeRepository).findById(anyLong()),
                () -> assertThat(teamDetailResponse.getLikeId()).isEqualTo(ID),
                () -> assertThat(teamDetailResponse.getTeamName()).isEqualTo(teamLike.getReceivedTeam().getName())
        );
    }

    @Test
    void 보낸팀_상세_조회_실패_호감_없음() {
        //given
        willThrow(new NotFoundException(ApplicationError.LIKE_NOT_FOUND)).given(teamLikeRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> likeService.getSentTeamDetail(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.LIKE_NOT_FOUND.getMessage());
    }

    @Test
    void 받은팀_상세_조회() {
        //given
        willReturn(Optional.of(teamLike)).given(teamLikeRepository).findById(anyLong());
        //when
        TeamDetailResponse teamDetailResponse = likeService.getReceivedTeamDetail(ID);
        //then
        assertAll(
                () -> verify(teamLikeRepository).findById(anyLong()),
                () -> assertThat(teamDetailResponse.getLikeId()).isEqualTo(ID),
                () -> assertThat(teamDetailResponse.getTeamName()).isEqualTo(teamLike.getSentTeam().getName())
        );
    }

    @Test
    void 받은팀_상세_조회_실패_호감_없음() {
        //given
        willThrow(new NotFoundException(ApplicationError.LIKE_NOT_FOUND)).given(teamLikeRepository).findById(anyLong());
        //when & then
        assertThatThrownBy(() -> likeService.getReceivedTeamDetail(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.LIKE_NOT_FOUND.getMessage());
    }
}
