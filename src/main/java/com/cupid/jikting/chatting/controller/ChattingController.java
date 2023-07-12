package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chattings")
public class ChattingController {

    private final ChattingService chattingService;

    @GetMapping
    public ResponseEntity<List<ChattingRoomResponse>> getAll() {
        return ResponseEntity.ok().body(chattingService.getAll());
    }

    @GetMapping("/{chattingRoomId}")
    public ResponseEntity<ChattingRoomDetailResponse> get(@PathVariable Long chattingRoomId) {
        return ResponseEntity.ok().body(chattingService.get(chattingRoomId));
    }

    @PostMapping("/{chattingRoomId}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long chattingRoomId) {
        chattingService.confirm(chattingRoomId);
        return ResponseEntity.ok().build();
    }
}
