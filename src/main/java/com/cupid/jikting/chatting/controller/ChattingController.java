package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChattingController {

    private final ChattingService chattingService;

    @MessageMapping("/chattings/rooms/{chattingRoomId}")
    public void send(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        chattingService.sendMessage(chattingRoomId, chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRoomId);
    }
}
