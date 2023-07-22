package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import org.springframework.stereotype.Service;

@Service
public interface ChattingService {

    void sendMessage(Long chattingRoomId, ChattingRequest chattingRequest);
}
