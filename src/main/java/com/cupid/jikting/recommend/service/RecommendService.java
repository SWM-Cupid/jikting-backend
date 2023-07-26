package com.cupid.jikting.recommend.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.recommend.dto.MemberResponse;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.team.entity.TeamPersonality;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private final MemberProfileRepository memberProfileRepository;

    public List<RecommendResponse> get(Long memberProfileId) {
        MemberProfile memberProfile = memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
        return memberProfile.getRecommends()
                .stream()
                .map(recommend -> RecommendResponse.builder()
                        .recommendId(recommend.getId())
                        .members(getMemberResponses(recommend))
                        .personalities(getTeamPersonalities(recommend))
                        .build())
                .collect(Collectors.toList());
    }

    public void sendLike(Long recommendId) {
    }

    private List<MemberResponse> getMemberResponses(Recommend recommend) {
        return recommend.getFrom()
                .getMemberProfiles()
                .stream()
                .map(MemberResponse::toMemberResponse)
                .collect(Collectors.toList());
    }

    private List<String> getTeamPersonalities(Recommend recommend) {
        return recommend.getFrom()
                .getTeamPersonalities()
                .stream()
                .map(TeamPersonality::getPersonality)
                .map(Personality::getKeyword)
                .collect(Collectors.toList());
    }
}
