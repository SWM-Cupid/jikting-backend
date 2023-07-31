package com.cupid.jikting.meeting.service;

import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.entity.InstantMeeting;
import com.cupid.jikting.meeting.repository.InstantMeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstantMeetingService {

    private final InstantMeetingRepository instantMeetingRepository;

    public List<InstantMeetingResponse> getAll(Long memberProfileId) {
        List<InstantMeeting> instantMeetings = getInstantMeeting();
        return instantMeetings.stream()
                .map(instantMeeting ->
                        InstantMeetingResponse.of(instantMeeting, isAttend(instantMeeting, memberProfileId)))
                .collect(Collectors.toList());
    }

    public void attend(Long instantMeetingId) {
    }

    private List<InstantMeeting> getInstantMeeting() {
        return instantMeetingRepository.findAll();
    }

    private boolean isAttend(InstantMeeting instantMeeting, Long memberProfileId) {
        return instantMeeting.getInstantMeetingMembers().stream()
                .anyMatch(instantMeetingMember -> instantMeetingMember.getMemberProfileId().equals(memberProfileId));
    }
}
