package com.cupid.jikting.recommend.service;

import com.cupid.jikting.recommend.dto.RecommendedTeamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendService {

    public List<RecommendedTeamResponse> getRecommendedTeam(Long teamId) {
        return null;
    }

    public void sendLike(Long recommendId) {
    }
}
