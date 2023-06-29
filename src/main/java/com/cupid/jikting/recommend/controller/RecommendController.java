package com.cupid.jikting.recommend.controller;

import com.cupid.jikting.recommend.dto.RecommendedTeamResponse;
import com.cupid.jikting.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recommends")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<List<RecommendedTeamResponse>> getRecommendedTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(recommendService.getRecommendedTeam(teamId));
    }

    @PostMapping("/{recommendId}/like")
    public ResponseEntity<Void> sendLike(@PathVariable Long recommendId) {
        recommendService.sendLike(recommendId);
        return ResponseEntity.ok().build();
    }
}
