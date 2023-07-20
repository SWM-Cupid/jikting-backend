package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.service.InstantMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instant-meetings")
public class InstantMeetingController {

    private final InstantMeetingService instantMeetingService;

    @GetMapping
    public ResponseEntity<List<InstantMeetingResponse>> getAll() {
        return ResponseEntity.ok().body(instantMeetingService.getAll());
    }

    @PostMapping("/{instantMeetingId}")
    public ResponseEntity<Void> attend(@PathVariable Long instantMeetingId) {
        instantMeetingService.attend(instantMeetingId);
        return ResponseEntity.ok().build();
    }
}
