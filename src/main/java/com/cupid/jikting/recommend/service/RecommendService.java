package com.cupid.jikting.recommend.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.recommend.repository.RecommendRepository;
import com.cupid.jikting.team.entity.AcceptStatus;
import com.cupid.jikting.team.entity.TeamLike;
import com.cupid.jikting.team.repository.TeamLikeRepository;
import com.cupid.jikting.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RecommendService {

    private final MemberProfileRepository memberProfileRepository;
    private final RecommendRepository recommendRepository;
    private final TeamLikeRepository teamLikeRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<RecommendResponse> get(Long memberProfileId) {
        return getMemberProfileById(memberProfileId).getRecommends()
                .stream()
                .map(RecommendResponse::from)
                .collect(Collectors.toList());
    }

    public void sendLike(Long recommendId) {
        Recommend recommend = recommendRepository.findById(recommendId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.RECOMMEND_NOT_FOUND));
        TeamLike teamLike = TeamLike.builder()
                .sentTeam(recommend.getTo())
                .receivedTeam(recommend.getFrom())
                .acceptStatus(AcceptStatus.INITIAL)
                .build();
        teamLikeRepository.save(teamLike);
        recommendRepository.deleteById(recommendId);
    }

    public void passLike(Long recommendId) {
        recommendRepository.deleteById(recommendId);
    }

    private MemberProfile getMemberProfileById(Long memberProfileId) {
        return memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }
}
