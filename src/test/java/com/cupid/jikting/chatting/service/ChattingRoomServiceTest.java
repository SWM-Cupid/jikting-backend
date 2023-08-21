package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.dto.MeetingConfirmRequest;
import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.error.WrongAccessException;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.meeting.entity.Meeting;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.entity.TeamPersonality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChattingRoomServiceTest {

    private static final Long ID = 1L;
    private static final Long WRONG_ID = 2L;
    private static final String NAME = "이름";
    private static final String NICKNAME = "닉네임";
    private static final String KEYWORD = "키워드";
    private static final int YEAR = 1967;
    private static final int MONTH = 5;
    private static final int DATE = 10;
    private static final int HEIGHT = 189;
    private static final String ADDRESS = "주소";
    private static final String COLLEGE = "대학";
    private static final String DESCRIPTION = "한 줄 소개";
    private static final boolean LEADER = true;
    private static final LocalDateTime SCHEDULE = LocalDateTime.of(2023, 9, 10, 18, 30);
    private static final String PLACE = "장소";
    private static final String LAST_MESSAGE = "마지막 메시지";
    private static final String CONTENT = "메시지 내용";

    private MemberProfile memberProfile;
    private ChattingRoom chattingRoom;
    private Chatting chatting;
    private List<ChattingRoom> chattingRooms;
    private ApplicationException memberNotFoundException;

    @InjectMocks
    private ChattingRoomService chattingRoomService;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private ChattingRoomRepository chattingRoomRepository;

    @Mock
    private RedisConnector redisConnector;

    @Mock
    private RedisMessageListener redisMessageListener;

    @BeforeEach
    void setUp() {
        Personality personality = Personality.builder()
                .keyword(KEYWORD)
                .build();
        TeamPersonality teamPersonality = TeamPersonality.builder()
                .personality(personality)
                .build();
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .build();
        team.addTeamPersonalities(List.of(teamPersonality));
        Hobby hobby = Hobby.builder()
                .keyword(KEYWORD)
                .build();
        MemberPersonality memberPersonality = MemberPersonality.builder()
                .personality(personality)
                .build();
        MemberHobby memberHobby = MemberHobby.builder()
                .hobby(hobby)
                .build();
        Member member = Member.builder().build();
        member.addMemberProfile(NICKNAME);
        memberProfile = member.getMemberProfile();
        memberProfile.updateProfile(LocalDate.of(YEAR, MONTH, DATE), HEIGHT, Mbti.ENFJ, ADDRESS, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                List.of(memberPersonality), List.of(memberHobby));
        TeamMember.of(LEADER, team, memberProfile);
        chattingRoom = ChattingRoom.builder()
                .id(ID)
                .meeting(Meeting.builder()
                        .id(ID)
                        .recommendedTeam(team)
                        .acceptingTeam(team)
                        .build())
                .build();
        chatting = Chatting.builder()
                .id(String.valueOf(ID))
                .senderId(ID)
                .content(CONTENT)
                .createdAt(LocalDateTime.now())
                .build();
        chattingRooms = IntStream.range(0, 1)
                .mapToObj(n -> chattingRoom)
                .collect(Collectors.toList());
        MemberChattingRoom.of(memberProfile, chattingRoom);
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
    }

    @Test
    void 채팅방_목록_조회_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(LAST_MESSAGE).given(redisConnector).getLastMessage(anyString());
        // when
        List<ChattingRoomResponse> chattingRoomResponses = chattingRoomService.getAll(ID);
        // then
        assertThat(chattingRoomResponses.size()).isEqualTo(chattingRooms.size());
    }

    @Test
    void 채팅방_목록_조회_실패_회원_프로필_없음() {
        // given
        willThrow(memberNotFoundException).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingRoomService.getAll(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 채팅방_입장_성공() {
        // given
        List<Chatting> chattings = List.of(chatting);
        willDoNothing().given(redisMessageListener).enterChattingRoom(anyLong());
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(chattings).given(redisConnector).getMessages(anyString());
        // when
        ChattingRoomDetailResponse chattingRoomDetailResponse = chattingRoomService.get(ID, ID);
        // then
        assertAll(
                () -> verify(redisMessageListener).enterChattingRoom(anyLong()),
                () -> verify(chattingRoomRepository).findById(anyLong()),
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(redisConnector).getMessages(anyString()),
                () -> assertThat(chattingRoomDetailResponse.getName()).isEqualTo(NAME),
                () -> assertThat(chattingRoomDetailResponse.getDescription()).isEqualTo(DESCRIPTION),
                () -> assertThat(chattingRoomDetailResponse.getKeywords().size()).isEqualTo(chattingRoom.getOppositeTeamKeywords(memberProfile.getTeam()).size()),
                () -> assertThat(chattingRoomDetailResponse.getMembers().size()).isEqualTo(chattingRoom.getMemberProfiles().size()),
                () -> assertThat(chattingRoomDetailResponse.getChattings().size()).isEqualTo(chattings.size())
        );
    }

    @Test
    void 채팅방_입장_실패_채팅방_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND)).given(chattingRoomRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingRoomService.get(ID, ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.CHATTING_ROOM_NOT_FOUND.getMessage());
    }

    @Test
    void 채팅방_입장_실패_회원_프로필_없음() {
        // given
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        willThrow(memberNotFoundException).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingRoomService.get(ID, ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 채팅방_내_미팅_확정_성공() {
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
    void 채팅방_내_미팅_확정_실패_채팅방_없음() {
        // given
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

    @Test
    void 채팅방_내_미팅_확정_실패_잘못된_미팅() {
        // given
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        MeetingConfirmRequest meetingConfirmRequest = MeetingConfirmRequest.builder()
                .meetingId(WRONG_ID)
                .schedule(SCHEDULE)
                .place(PLACE)
                .build();
        // when & then
        assertThatThrownBy(() -> chattingRoomService.confirm(ID, meetingConfirmRequest))
                .isInstanceOf(WrongAccessException.class)
                .hasMessage(ApplicationError.WRONG_ACCESS.getMessage());
    }
}
