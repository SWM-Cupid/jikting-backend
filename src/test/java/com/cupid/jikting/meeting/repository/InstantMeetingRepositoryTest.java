package com.cupid.jikting.meeting.repository;

import com.cupid.jikting.meeting.entity.InstantMeeting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InstantMeetingRepositoryTest {

    private static final LocalDateTime START = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    private static final LocalDateTime END = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

    @Autowired
    InstantMeetingRepository instantMeetingRepository;

    @Test
    void 번개팅_조회_성공() {
        // given
        InstantMeeting instantMeeting1 = InstantMeeting.builder()
                .schedule(START.plusHours(1))
                .build();
        InstantMeeting instantMeeting2 = InstantMeeting.builder()
                .schedule(START.plusMinutes(10))
                .build();
        InstantMeeting instantMeeting3 = InstantMeeting.builder()
                .schedule(START.minusHours(1))
                .build();
        instantMeetingRepository.saveAll(List.of(instantMeeting1, instantMeeting2, instantMeeting3));
        // when
        List<InstantMeeting> instantMeetings = instantMeetingRepository.findAllByScheduleBetween(START, END);
        // then
        assertThat(instantMeetings.size()).isEqualTo(2);
    }
}
