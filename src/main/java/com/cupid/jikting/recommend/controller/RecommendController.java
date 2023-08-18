package com.cupid.jikting.recommend.controller;

import com.cupid.jikting.common.support.AuthorizedVariable;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recommends")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public ResponseEntity<List<RecommendResponse>> get(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(recommendService.get(memberProfileId));
    }

    @PostMapping("/{recommendId}/like")
    public ResponseEntity<Void> sendLike(@PathVariable Long recommendId) {
        recommendService.sendLike(recommendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recommendId}/pass")
    public ResponseEntity<Void> passLike(@PathVariable Long recommendId) {
        recommendService.passLike(recommendId);
        return ResponseEntity.ok().build();
    }
}
