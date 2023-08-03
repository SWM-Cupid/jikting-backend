package com.cupid.jikting.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsResponse {

    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}
