package com.cupid.jikting.meeting.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.entity.InstantMeeting;
import com.cupid.jikting.meeting.entity.InstantMeetingMember;
import com.cupid.jikting.meeting.repository.InstantMeetingRepository;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InstantMeetingServiceTest {

    private static final Long ID = 1L;
    private static final LocalDateTime SCHEDULE = LocalDateTime.of(2023, Month.JUNE, 30, 18, 0);
    private static final String PLACE = "장소";

    private List<InstantMeeting> instantMeetings;
    private MemberProfile memberProfile;
    private InstantMeeting instantMeeting;

    @InjectMocks
    InstantMeetingService instantMeetingService;

    @Mock
    InstantMeetingRepository instantMeetingRepository;

    @Mock
    MemberProfileRepository memberProfileRepository;

    @BeforeEach
    void setUp() {
        memberProfile = MemberProfile.builder()
                .id(ID)
                .build();
        instantMeeting = InstantMeeting.builder()
                .id(ID)
                .schedule(SCHEDULE)
                .place(PLACE)
                .build();
        InstantMeetingMember.of(instantMeeting, memberProfile);
        instantMeetings = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> instantMeeting)
                .collect(Collectors.toList());
    }

    @Test
    void 번개팅_조회_성공() {
        // given
        willReturn(instantMeetings).given(instantMeetingRepository).findAll();
        // when
        List<InstantMeetingResponse> instantMeetingResponses = instantMeetingService.getAll(ID);
        // then
        assertAll(
                () -> verify(instantMeetingRepository).findAll(),
                () -> assertThat(instantMeetingResponses.size()).isEqualTo(instantMeetings.size())
        );
    }

    @Test
    void 번개팅_참여_성공() {
        //given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(instantMeeting)).given(instantMeetingRepository).findById(anyLong());
        //when
        instantMeetingService.attend(ID, ID);
        //then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(instantMeetingRepository).findById(anyLong()),
                () -> verify(memberProfileRepository).save(any(MemberProfile.class))
        );
    }

    @Test
    void 번개팅_참여_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> instantMeetingService.attend(ID, ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 번개팅_참여_실패_번개팅_없음() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willThrow(new NotFoundException(ApplicationError.INSTANT_MEETING_NOT_FOUND)).given(instantMeetingRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> instantMeetingService.attend(ID, ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.INSTANT_MEETING_NOT_FOUND.getMessage());
    }
}
