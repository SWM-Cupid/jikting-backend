package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.common.support.AuthorizedVariable;
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
    public ResponseEntity<List<InstantMeetingResponse>> getAll(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(instantMeetingService.getAll(memberProfileId));
    }

    @PostMapping("/{instantMeetingId}")
    public ResponseEntity<Void> attend(@AuthorizedVariable Long memberProfileId, @PathVariable Long instantMeetingId) {
        instantMeetingService.attend(memberProfileId, instantMeetingId);
        return ResponseEntity.ok().build();
    }
}
