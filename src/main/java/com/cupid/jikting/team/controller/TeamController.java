package com.cupid.jikting.team.controller;

import com.cupid.jikting.team.dto.TeamProfileResponse;
import com.cupid.jikting.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/likes/received")
    public ResponseEntity<List<TeamProfileResponse>> getReceivedLikes() {
        return ResponseEntity.ok().body(meetingService.getReceivedLikes());
    }

    @GetMapping("/likes/sent")
    public ResponseEntity<List<TeamProfileResponse>> getSentLikes() {
        return ResponseEntity.ok().body(meetingService.getSentLikes());
    }
}
