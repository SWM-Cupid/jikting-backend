package com.cupid.jikting.instantmeeting.controller;

import com.cupid.jikting.instantmeeting.dto.InstantMeetingResponse;
import com.cupid.jikting.instantmeeting.service.InstantMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instants")
public class InstantMeetingController {

    private final InstantMeetingService instantMeetingService;

    @GetMapping
    public ResponseEntity<List<InstantMeetingResponse>> getAll() {
        return ResponseEntity.ok().body(instantMeetingService.getAll());
    }
}
