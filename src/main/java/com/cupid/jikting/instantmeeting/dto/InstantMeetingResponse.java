package com.cupid.jikting.instantmeeting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstantMeetingResponse {

    private Long instantMeetingId;
    private LocalDateTime instantMeetingDateTime;
    private String location;
}
