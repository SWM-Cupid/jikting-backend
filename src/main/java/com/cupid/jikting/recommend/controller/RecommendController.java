package com.cupid.jikting.recommend.controller;

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
    public ResponseEntity<List<RecommendResponse>> get() {
        return ResponseEntity.ok().body(recommendService.get());
    }

    @PostMapping("/{recommendId}/like")
    public ResponseEntity<Void> sendLike(@PathVariable Long recommendId) {
        recommendService.sendLike(recommendId);
        return ResponseEntity.ok().build();
    }
}
