package com.cupid.jikting.like.service;

import com.cupid.jikting.like.dto.LikeResponse;
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

    public void declineLike(Long likeId) {
    }
}
