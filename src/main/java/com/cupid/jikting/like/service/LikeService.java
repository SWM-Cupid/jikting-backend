package com.cupid.jikting.like.service;

import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    public List<LikeResponse> getAllReceivedLike() {
        return null;
    }

    public List<LikeResponse> getAllSentLike() {
        return null;
    }

    public void acceptLike(Long likeId) {
    }

    public void rejectLike(Long likeId) {
    }

    public TeamDetailResponse getTeamDetail(Long likeId) {
        return null;
    }
}
