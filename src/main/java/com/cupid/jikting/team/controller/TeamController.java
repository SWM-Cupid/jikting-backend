package com.cupid.jikting.team.controller;

import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.dto.TeamUpdateRequest;
import com.cupid.jikting.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<TeamRegisterResponse> register(@RequestHeader("Authorization") String token,
                                                         @RequestBody TeamRegisterRequest teamRegisterRequest) {
        return ResponseEntity.ok()
                .body(teamService.register(jwtService.extractValidMemberProfileId(token), teamRegisterRequest));
    }

    @PostMapping("/{teamId}/attend")
    public ResponseEntity<Void> attend(@RequestHeader("Authorization") String token, @PathVariable Long teamId) {
        teamService.attend(teamId, jwtService.extractValidMemberProfileId(token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> get(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(teamService.get(teamId));
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<Void> update(@PathVariable Long teamId, @RequestBody TeamUpdateRequest teamUpdateRequest) {
        teamService.update(teamId, teamUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable Long teamId) {
        teamService.delete(teamId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long teamId, @PathVariable Long memberId) {
        teamService.deleteMember(teamId, memberId);
        return ResponseEntity.ok().build();
    }
}
