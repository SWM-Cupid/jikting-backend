package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChattingController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChattingService chattingService;

    @MessageMapping("/chattings/rooms/{chattingRoomId}/messages")
    public void send(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        simpMessagingTemplate.convertAndSend("/subscription/chattings/rooms/" + chattingRoomId, chattingRequest.getContent());
        chattingService.sendMessage(chattingRoomId, chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRoomId);
    }
}
