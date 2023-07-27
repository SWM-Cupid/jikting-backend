package com.cupid.jikting.like.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final TeamMemberRepository teamMemberRepository;

    public List<LikeResponse> getAllReceivedLike(Long memberProfileId) {
        Long teamId = teamMemberRepository.getTeamIdByMemberProfileId(memberProfileId)
                .orElseThrow(() -> new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM));
        log.info("-----------teamID: " + teamId);
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
