package com.cupid.jikting.meeting.dto;

import com.cupid.jikting.meeting.entity.InstantMeeting;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstantMeetingResponse {

    private Long instantMeetingId;
    private LocalDateTime schedule;
    private String place;
    private int memberCount;
    private boolean isAttend;

    public static InstantMeetingResponse of(InstantMeeting instantMeeting, boolean isAttend) {
        return new InstantMeetingResponse(
                instantMeeting.getId(),
                instantMeeting.getSchedule(),
                instantMeeting.getPlace(),
                instantMeeting.getMemberCount(),
                isAttend);
    }
}
