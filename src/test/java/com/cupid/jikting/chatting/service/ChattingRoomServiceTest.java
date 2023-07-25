package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;

@WebMvcTest(MockitoExtension.class)
class ChattingRoomServiceTest {

    private static final Long ID = 1L;
    private static final String TEAM_NAME = "팀 이름";

    private MemberProfile memberProfile;
    private List<ChattingRoom> chattingRooms;

    @InjectMocks
    private ChattingRoomService chattingRoomService;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private ChattingRoomRepository chattingRoomRepository;

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
        chattingRooms = IntStream.range(0, 3)
                .mapToObj(n -> ChattingRoom.builder()
                        .id(n + ID)
                        .meeting(Meeting.builder()
                                .id(ID)
                                .recommendedTeam(team)
                                .acceptingTeam(team)
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Test
    void 채팅_목록_조회_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(chattingRooms).given(chattingRoomRepository).findAll();
        // when
        List<ChattingRoomResponse> chattingRoomResponses = chattingRoomService.getAll(ID);
        // then
        assertThat(chattingRoomResponses.size()).isEqualTo(3);
    }
}
