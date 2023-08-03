package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.dto.MeetingConfirmRequest;
import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.entity.MemberChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.entity.ProfileImage;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChattingRoomService {

    private static final String NO_MESSAGE = "";

    private MemberProfileRepository memberProfileRepository;
    private ChattingRoomRepository chattingRoomRepository;

    public List<ChattingRoomResponse> getAll(Long memberProfileId) {
        MemberProfile memberProfile = memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
        return chattingRoomRepository.findAll()
                .stream()
                .map(chattingRoom -> toChattingRoomResponse(chattingRoom, memberProfile.getTeam()))
                .collect(Collectors.toList());
    }

    public ChattingRoomDetailResponse get(Long chattingRoomId) {
        return null;
    }

    public void confirm(Long chattingRoomId, MeetingConfirmRequest meetingConfirmRequest) {
        ChattingRoom chattingRoom = getChattingRoomById(chattingRoomId);
        chattingRoom.confirmMeeting(meetingConfirmRequest.getMeetingId(), meetingConfirmRequest.getSchedule(), meetingConfirmRequest.getPlace());
        chattingRoomRepository.save(chattingRoom);
    }

    private ChattingRoomResponse toChattingRoomResponse(ChattingRoom chattingRoom, Team team) {
        return ChattingRoomResponse.builder()
                .chattingRoomId(chattingRoom.getId())
                .opposingTeamName(chattingRoom.getOppositeTeamName(team))
                .lastMessage(getLastMessage(chattingRoom.getChattings()))
                .images(getImages(chattingRoom.getMemberChattingRooms()))
                .build();
    }

    private String getLastMessage(List<Chatting> chattings) {
        if (chattings.isEmpty()) {
            return NO_MESSAGE;
        }
        return chattings.get(chattings.size() - 1).getContent();
    }

    private List<String> getImages(List<MemberChattingRoom> memberChattingRooms) {
        return memberChattingRooms.stream()
                .flatMap(memberChattingRoom -> Arrays.stream(memberChattingRoom.getMemberProfile()
                        .getProfileImages()
                        .stream()
                        .map(ProfileImage::getUrl)
                        .toArray(String[]::new)))
                .collect(Collectors.toList());
    }

    private ChattingRoom getChattingRoomById(Long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND));
    }
}
