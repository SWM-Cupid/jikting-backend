package com.cupid.jikting.team.controller;

import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
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

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<TeamRegisterResponse> deleteMember(@PathVariable Long teamId, @PathVariable Long memberId) {
        teamService.deleteMember(teamId, memberId);
        return ResponseEntity.ok().build();
    }
}
