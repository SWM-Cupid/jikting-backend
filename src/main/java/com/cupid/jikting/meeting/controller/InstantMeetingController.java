package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.jwt.service.JwtService;
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
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<InstantMeetingResponse>> getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(instantMeetingService.getAll(jwtService.extractValidMemberProfileId(token)));
    }

    @PostMapping("/{instantMeetingId}")
    public ResponseEntity<Void> attend(@RequestHeader("Authorization") String token, @PathVariable Long instantMeetingId) {
        instantMeetingService.attend(jwtService.extractValidMemberProfileId(token), instantMeetingId);
        return ResponseEntity.ok().build();
    }
}
