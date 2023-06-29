package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.meeting.dto.TeamProfileResponse;
import com.cupid.jikting.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/likes/received")
    public ResponseEntity<List<TeamProfileResponse>> getReceivedLikes() {
        return ResponseEntity.ok().body(meetingService.getReceivedLikes());
    }

}
