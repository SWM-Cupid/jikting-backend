package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingRoomDetailResponse;
import com.cupid.jikting.chatting.dto.ChattingRoomResponse;
import com.cupid.jikting.chatting.dto.MeetingConfirmRequest;
import com.cupid.jikting.chatting.service.ChattingRoomService;
import com.cupid.jikting.common.support.AuthorizedVariable;
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
    public ResponseEntity<List<ChattingRoomResponse>> getAll(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(chattingRoomService.getAll(memberProfileId));
    }

    @GetMapping("/{chattingRoomId}")
    public ResponseEntity<ChattingRoomDetailResponse> get(@AuthorizedVariable Long memberProfileId, @PathVariable Long chattingRoomId) {
        return ResponseEntity.ok().body(chattingRoomService.get(memberProfileId, chattingRoomId));
    }

    @PostMapping("/{chattingRoomId}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long chattingRoomId, MeetingConfirmRequest meetingConfirmRequest) {
        chattingRoomService.confirm(chattingRoomId, meetingConfirmRequest);
        return ResponseEntity.ok().build();
    }
}
