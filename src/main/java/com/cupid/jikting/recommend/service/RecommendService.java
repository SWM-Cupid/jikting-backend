package com.cupid.jikting.recommend.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.recommend.dto.RecommendResponse;
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
                .map(RecommendResponse::from)
                .collect(Collectors.toList());
    }

    public void sendLike(Long recommendId) {
    }
}
