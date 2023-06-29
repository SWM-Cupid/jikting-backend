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

    @GetMapping("/{teamId}")
    public ResponseEntity<RecommendedTeamResponse> getRecommendedTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok().body(recommendService.getRecommendedTeam(teamId));
    }
}
