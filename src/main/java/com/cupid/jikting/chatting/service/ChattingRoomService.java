package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChattingRoomService {

    public List<ChattingRoomResponse> getAll() {
        return null;
    }

    public ChattingRoomDetailResponse get(Long chattingRoomId) {
        return null;
    }

    public void confirm(Long chattingRoomId) {
    }
}
