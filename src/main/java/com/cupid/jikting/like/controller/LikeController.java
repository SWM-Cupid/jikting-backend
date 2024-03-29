package com.cupid.jikting.like.controller;

import com.cupid.jikting.common.support.AuthorizedVariable;
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
    public ResponseEntity<List<LikeResponse>> getAllReceivedLike(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(likeService.getAllReceivedLike(memberProfileId));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<LikeResponse>> getAllSentLike(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(likeService.getAllSentLike(memberProfileId));
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

    @GetMapping("/{likeId}/sent")
    public ResponseEntity<TeamDetailResponse> getSentTeamDetail(@PathVariable Long likeId) {
        return ResponseEntity.ok().body(likeService.getSentTeamDetail(likeId));
    }

    @GetMapping("/{likeId}/received")
    public ResponseEntity<TeamDetailResponse> getReceivedTeamDetail(@PathVariable Long likeId) {
        return ResponseEntity.ok().body(likeService.getReceivedTeamDetail(likeId));
    }
}
