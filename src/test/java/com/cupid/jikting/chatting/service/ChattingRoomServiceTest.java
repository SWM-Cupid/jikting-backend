package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.dto.MeetingConfirmRequest;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@WebMvcTest(MockitoExtension.class)
class ChattingRoomServiceTest {

    private static final Long ID = 1L;
    private static final String TEAM_NAME = "팀 이름";
    private static final boolean LEADER = true;
    private static final LocalDateTime SCHEDULE = LocalDateTime.of(2023, 9, 10, 18, 30);
    private static final String PLACE = "장소";

    private MemberProfile memberProfile;
    private ChattingRoom chattingRoom;
    private List<ChattingRoom> chattingRooms;
    private ApplicationException memberNotFoundException;

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
        memberProfile = MemberProfile.builder()
                .id(ID)
                .build();
        TeamMember.of(LEADER, team, memberProfile);
        chattingRoom = ChattingRoom.builder()
                .id(ID)
                .meeting(Meeting.builder()
                        .id(ID)
                        .recommendedTeam(team)
                        .acceptingTeam(team)
                        .build())
                .build();
        chattingRooms = IntStream.range(0, 3)
                .mapToObj(n -> chattingRoom)
                .collect(Collectors.toList());
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
    }

    @Test
    void 채팅_목록_조회_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(chattingRooms).given(chattingRoomRepository).findAll();
        // when
        List<ChattingRoomResponse> chattingRoomResponses = chattingRoomService.getAll(ID);
        // then
        assertThat(chattingRoomResponses.size()).isEqualTo(chattingRooms.size());
    }

    @Test
    void 채팅_목록_조회_실패_회원_프로필_없음() {
        // given
        willThrow(memberNotFoundException).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingRoomService.getAll(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 채팅방_내_미팅_화정_성공() {
        // given
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        willReturn(chattingRoom).given(chattingRoomRepository).save(any(ChattingRoom.class));
        MeetingConfirmRequest meetingConfirmRequest = MeetingConfirmRequest.builder()
                .meetingId(ID)
                .schedule(SCHEDULE)
                .place(PLACE)
                .build();
        // when
        chattingRoomService.confirm(ID, meetingConfirmRequest);
        // then
        assertAll(
                () -> verify(chattingRoomRepository).findById(anyLong()),
                () -> verify(chattingRoomRepository).save(any(ChattingRoom.class))
        );
    }

    @Test
    void 채팅방_내_미팅_화정_실패_채팅방_없음() {
        // given
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        willThrow(new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND)).given(chattingRoomRepository).findById(anyLong());
        MeetingConfirmRequest meetingConfirmRequest = MeetingConfirmRequest.builder()
                .meetingId(ID)
                .schedule(SCHEDULE)
                .place(PLACE)
                .build();
        // when & then
        assertThatThrownBy(() -> chattingRoomService.confirm(ID, meetingConfirmRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.CHATTING_ROOM_NOT_FOUND.getMessage());
    }
}
