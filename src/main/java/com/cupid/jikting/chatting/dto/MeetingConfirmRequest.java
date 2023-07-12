package com.cupid.jikting.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingConfirmRequest {

    private Long meetingId;
    private String place;
    private LocalDateTime schedule;
}
