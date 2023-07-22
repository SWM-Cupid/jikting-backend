package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StompChattingService implements ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Transactional
    public void sendMessage(Long chattingRoomId, ChattingRequest chattingRequest) {
        Member sender = memberRepository.findById(chattingRequest.getSenderId())
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND));
        Chatting chatting = Chatting.builder()
                .member(sender)
                .chattingRoom(chattingRoom)
                .content(chattingRequest.getContent())
                .build();
        chattingRoom.createChatting(chatting);
        chattingRoomRepository.save(chattingRoom);
    }
}
