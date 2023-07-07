package com.cupid.jikting.team.controller;

import com.cupid.jikting.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/attend/{teamId}")
    public ResponseEntity<Void> attend(@PathVariable Long teamId) {
        teamService.attend(teamId);
        return ResponseEntity.ok().build();
    }
}