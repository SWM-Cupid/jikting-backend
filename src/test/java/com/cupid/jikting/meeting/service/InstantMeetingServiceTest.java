package com.cupid.jikting.meeting.service;

import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.entity.InstantMeeting;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
import com.cupid.jikting.meeting.repository.InstantMeetingRepository;
import com.cupid.jikting.member.dto.MemberResponse;
import com.cupid.jikting.member.entity.MemberProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InstantMeetingServiceTest {

    private static final Long ID = 1L;
    private static final LocalDateTime SCHEDULE = LocalDateTime.of(2023, Month.JUNE, 30, 18, 0);
    private static final String PLACE = "장소";

    private List<InstantMeeting> instantMeetings;

    @InjectMocks
    InstantMeetingService instantMeetingService;

    @Mock
    InstantMeetingRepository instantMeetingRepository;

    @BeforeEach
    void setUp() {
        MemberProfile memberProfile = MemberProfile.builder()
                .id(ID)
                .build();
        InstantMeetingMember instantMeetingMember = InstantMeetingMember.builder()
                .memberProfile(memberProfile)
                .build();
        InstantMeeting instantMeeting = InstantMeeting.builder()
                .id(ID)
                .schedule(SCHEDULE)
                .place(PLACE)
                .instantMeetingMembers(List.of(instantMeetingMember))
                .build();
        instantMeetings = List.of(instantMeeting, instantMeeting);
    }

    @Test
    void 번개팅_조회_성공() {
        // given
        willReturn(instantMeetings).given(instantMeetingRepository).findAllByScheduleBetween(any(), any());
        // when
        List<InstantMeetingResponse> instantMeetingResponses = instantMeetingService.getAll(ID);
        // then
        assertAll(
                () -> verify(instantMeetingRepository).findAllByScheduleBetween(any(), any()),
                () -> assertThat(instantMeetingResponses.size()).isEqualTo(instantMeetings.size())
        );
    }
}
