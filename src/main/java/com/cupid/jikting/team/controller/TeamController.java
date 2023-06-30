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

    @GetMapping("/{teamId}/likes/received")
    public ResponseEntity<List<TeamProfileResponse>> getReceivedLikes(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(teamService.getReceivedLikes(teamId));
    }

    @GetMapping("/{teamId}/likes/sent")
    public ResponseEntity<List<TeamProfileResponse>> getSentLikes(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(teamService.getSentLikes(teamId));
    }
}
