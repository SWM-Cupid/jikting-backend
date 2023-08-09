package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.common.support.AuthVariable;
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
    public ResponseEntity<List<InstantMeetingResponse>> getAll(@AuthVariable Long memberProfileId) {
        return ResponseEntity.ok().body(instantMeetingService.getAll(memberProfileId));
    }

    @PostMapping("/{instantMeetingId}")
    public ResponseEntity<Void> attend(@AuthVariable Long memberProfileId, @PathVariable Long instantMeetingId) {
        instantMeetingService.attend(memberProfileId, instantMeetingId);
        return ResponseEntity.ok().build();
    }
}
