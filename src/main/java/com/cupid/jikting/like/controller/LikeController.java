package com.cupid.jikting.like.controller;

import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/received")
    public ResponseEntity<List<LikeResponse>> getAllReceivedLike() {
        return ResponseEntity.ok().body(likeService.getAllReceivedLike());
    }

    @GetMapping("/sent")
    public ResponseEntity<List<LikeResponse>> getAllSentLike() {
        return ResponseEntity.ok().body(likeService.getAllSentLike());
    }

    @PostMapping("/{likeId}/accept")
    public ResponseEntity<Void> acceptLike(@PathVariable Long likeId) {
        likeService.acceptLike(likeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{likeId}/detail")
    public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable Long likeId) {
        return ResponseEntity.ok().body(likeService.getTeamDetail(likeId));
    }
}
