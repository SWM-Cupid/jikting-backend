package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.chatting.dto.ChattingResponse;
import com.cupid.jikting.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chattings")
public class ChattingController {

    private final ChattingService chattingService;

    @GetMapping
    public ResponseEntity<List<ChattingResponse>> getAll() {
        return ResponseEntity.ok().body(chattingService.getAll());
    }
}
