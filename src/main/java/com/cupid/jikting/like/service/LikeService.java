package com.cupid.jikting.like.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.team.entity.TeamLike;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.TeamLikeRepository;
import com.cupid.jikting.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final TeamMemberRepository teamMemberRepository;

    public List<LikeResponse> getAllReceivedLike(Long memberProfileId) {
        TeamMember teamMember = teamMemberRepository.getTeamMemberByMemberProfileId(memberProfileId)
                .orElseThrow(() -> new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM));
        return teamMember.getReceivedTeamLikes().stream()
                .map(LikeResponse::of)
                .collect(Collectors.toList());
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
