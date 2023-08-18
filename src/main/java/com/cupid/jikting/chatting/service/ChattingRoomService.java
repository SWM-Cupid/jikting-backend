package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.*;
import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ChattingRoomService {

    private static final String CHATTING_ROOM_KEY_HEADER = "CHATTING_ROOM:";
    private static final String YEAR = "년";
    private static final String MONTH = "월";
    private static final String DAY = "일";
    private static final String TIME_DELIMITER = ":";

    private final MemberProfileRepository memberProfileRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final RedisConnector redisConnector;
    private final RedisMessageListener redisMessageListener;

    public List<ChattingRoomResponse> getAll(Long memberProfileId) {
        MemberProfile memberProfile = getMemberProfileById(memberProfileId);
        return memberProfile.getChattingRooms()
                .stream()
                .map(chattingRoom -> getChattingRoomResponse(chattingRoom, memberProfile.getTeam()))
                .collect(Collectors.toList());
    }

    public ChattingRoomDetailResponse get(Long memberProfileId, Long chattingRoomId) {
        redisMessageListener.enterChattingRoom(chattingRoomId);
        ChattingRoom chattingRoom = getChattingRoomById(chattingRoomId);
        Team team = getMemberProfileById(memberProfileId).getTeam();
        return ChattingRoomDetailResponse.builder()
                .name(chattingRoom.getOppositeTeamName(team))
                .description(chattingRoom.getOppositeTeamDescription(team))
                .keywords(chattingRoom.getOppositeTeamKeywords(team))
                .members(getMemberResponses(chattingRoom))
                .chattings(getChattingResponses(chattingRoomId))
                .build();
    }

    public void confirm(Long chattingRoomId, MeetingConfirmRequest meetingConfirmRequest) {
        ChattingRoom chattingRoom = getChattingRoomById(chattingRoomId);
        chattingRoom.confirmMeeting(meetingConfirmRequest.getMeetingId(), meetingConfirmRequest.getSchedule(), meetingConfirmRequest.getPlace());
        chattingRoomRepository.save(chattingRoom);
    }

    private MemberProfile getMemberProfileById(Long memberProfileId) {
        return memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    private ChattingRoomResponse getChattingRoomResponse(ChattingRoom chattingRoom, Team team) {
        return ChattingRoomResponse.builder()
                .chattingRoomId(chattingRoom.getId())
                .opposingTeamName(chattingRoom.getOppositeTeamName(team))
                .lastMessage(redisConnector.getLastMessage(CHATTING_ROOM_KEY_HEADER + chattingRoom.getId()))
                .images(chattingRoom.getOppositeTeamImageUrls(team))
                .build();
    }

    private ChattingRoom getChattingRoomById(Long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND));
    }

    private List<MemberResponse> getMemberResponses(ChattingRoom chattingRoom) {
        return chattingRoom.getMemberProfiles()
                .stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    private List<ChattingResponse> getChattingResponses(Long chattingRoomId) {
        return redisConnector.getMessages(CHATTING_ROOM_KEY_HEADER + chattingRoomId)
                .stream()
                .sorted(Comparator.comparing(Chatting::getCreatedAt).reversed())
                .map(message -> ChattingResponse.builder()
                        .senderId(message.getSenderId())
                        .content(message.getContent())
                        .createdDate(toCreatedDate(message.getCreatedAt()))
                        .createdTime(toCreatedTime(message.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    private String toCreatedTime(LocalDateTime createdAt) {
        return createdAt.getYear() + YEAR + createdAt.getMonthValue() + MONTH + createdAt.getDayOfMonth() + DAY;
    }

    private String toCreatedDate(LocalDateTime createdAt) {
        return createdAt.getHour() + TIME_DELIMITER + createdAt.getMinute();
    }
}
