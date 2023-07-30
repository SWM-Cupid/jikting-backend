package com.cupid.jikting.meeting.repository;

import com.cupid.jikting.meeting.entity.InstantMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InstantMeetingRepository extends JpaRepository<InstantMeeting, Long> {

    List<InstantMeeting> findAllByScheduleBetween(LocalDateTime start, LocalDateTime end);
}
