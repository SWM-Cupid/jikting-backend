package com.cupid.jikting.like.controller;

import com.cupid.jikting.like.dto.LikeResponse;
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
    public ResponseEntity<List<LikeResponse>> getReceivedLikes() {
        return ResponseEntity.ok().body(likeService.getReceivedLikes());
    }

    @GetMapping("/sent")
    public ResponseEntity<List<LikeResponse>> getSentLikes() {
        return ResponseEntity.ok().body(likeService.getSentLikes());
    }

    @PostMapping("/{likeId}/accept")
    public ResponseEntity<Void> acceptLike(@PathVariable Long likeId) {
        likeService.acceptLike(likeId);
        return ResponseEntity.ok().build();
    }
}
