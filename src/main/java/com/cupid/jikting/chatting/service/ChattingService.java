package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;

public interface ChattingService {

    void sendMessage(Long chattingRoomId, ChattingRequest chattingRequest);
}
