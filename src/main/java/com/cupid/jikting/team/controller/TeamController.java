package com.cupid.jikting.team.controller;

import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamRegisterResponse> register(@RequestBody TeamRegisterRequest teamRegisterRequest) {
        return ResponseEntity.ok().body(teamService.register(teamRegisterRequest));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> get(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(teamService.get(teamId));
    }
}
