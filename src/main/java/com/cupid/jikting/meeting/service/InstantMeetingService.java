package com.cupid.jikting.meeting.service;

import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
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
        return instantMeetingRepository.findAll()
                .stream()
                .map(instantMeeting ->
                        InstantMeetingResponse.of(instantMeeting, memberProfileId))
                .collect(Collectors.toList());
    }

    public void attend(Long instantMeetingId) {
    }
}
