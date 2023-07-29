package com.cupid.jikting.like.controller;

import com.cupid.jikting.jwt.service.JwtService;
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
    private final JwtService jwtService;

    @GetMapping("/received")
    public ResponseEntity<List<LikeResponse>> getAllReceivedLike(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(likeService.getAllReceivedLike(jwtService.extractValidMemberProfileId(token)));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<LikeResponse>> getAllSentLike(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(likeService.getAllSentLike(jwtService.extractValidMemberProfileId(token)));
    }

    @PostMapping("/{likeId}/accept")
    public ResponseEntity<Void> acceptLike(@PathVariable Long likeId) {
        likeService.acceptLike(likeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{likeId}/reject")
    public ResponseEntity<Void> rejectLike(@PathVariable Long likeId) {
        likeService.rejectLike(likeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable Long likeId) {
        return ResponseEntity.ok().body(likeService.getTeamDetail(likeId));
    }
}
