package com.cupid.jikting.meeting.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.entity.InstantMeeting;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
import com.cupid.jikting.meeting.repository.InstantMeetingRepository;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstantMeetingService {

    private final InstantMeetingRepository instantMeetingRepository;
    private final MemberProfileRepository memberProfileRepository;

    public List<InstantMeetingResponse> getAll(Long memberProfileId) {
        return instantMeetingRepository.findAll()
                .stream()
                .map(instantMeeting -> InstantMeetingResponse.of(instantMeeting, memberProfileId))
                .collect(Collectors.toList());
    }

    @Transactional
    public void attend(Long memberProfileId, Long instantMeetingId) {
        MemberProfile memberProfile = getMemberProfileById(memberProfileId);
        InstantMeeting instantMeeting = getInstantMeetingById(instantMeetingId);
        InstantMeetingMember.of(instantMeeting, memberProfile);
        memberProfileRepository.save(memberProfile);
    }

    private MemberProfile getMemberProfileById(Long memberProfileId) {
        return memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }

    private InstantMeeting getInstantMeetingById(Long instantMeetingId) {
        return instantMeetingRepository.findById(instantMeetingId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.INSTANT_MEETING_NOT_FOUND));
    }
}
