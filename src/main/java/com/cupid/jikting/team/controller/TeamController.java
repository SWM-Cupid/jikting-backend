package com.cupid.jikting.team.controller;

import com.cupid.jikting.common.support.AuthorizedVariable;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
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

    @PostMapping
    public ResponseEntity<Void> register(@AuthorizedVariable Long memberProfileId,
                                         @RequestBody TeamRegisterRequest teamRegisterRequest) {
        teamService.register(memberProfileId, teamRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{teamId}/attend")
    public ResponseEntity<Void> attend(@AuthorizedVariable Long memberProfileId, @PathVariable Long teamId) {
        teamService.attend(teamId, memberProfileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<TeamResponse> get(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(teamService.get(memberProfileId));
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

    @DeleteMapping("/{teamId}/members/{memberProfileId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long teamId, @PathVariable Long memberProfileId) {
        teamService.deleteMember(teamId, memberProfileId);
        return ResponseEntity.ok().build();
    }
}
