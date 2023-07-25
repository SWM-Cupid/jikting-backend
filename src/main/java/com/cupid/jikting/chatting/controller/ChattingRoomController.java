package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chattings/rooms")
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @GetMapping
    public ResponseEntity<List<ChattingRoomResponse>> getAll() {
        return ResponseEntity.ok().body(chattingRoomService.getAll(1L));
    }

    @GetMapping("/{chattingRoomId}")
    public ResponseEntity<ChattingRoomDetailResponse> get(@PathVariable Long chattingRoomId) {
        return ResponseEntity.ok().body(chattingRoomService.get(chattingRoomId));
    }

    @PostMapping("/{chattingRoomId}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long chattingRoomId) {
        chattingRoomService.confirm(chattingRoomId);
        return ResponseEntity.ok().build();
    }
}
