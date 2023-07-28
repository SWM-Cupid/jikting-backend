package com.cupid.jikting.like.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final TeamMemberRepository teamMemberRepository;

    @Transactional(readOnly=true)
    public List<LikeResponse> getAllReceivedLike(Long memberProfileId) {
        return getTeamMemberById(memberProfileId)
                .getReceivedTeamLikes()
                .stream()
                .map(LikeResponse::from)
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

    private TeamMember getTeamMemberById(Long memberProfileId) {
        return teamMemberRepository.getTeamMemberByMemberProfileId(memberProfileId)
                .orElseThrow(() -> new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM));
    }
}
