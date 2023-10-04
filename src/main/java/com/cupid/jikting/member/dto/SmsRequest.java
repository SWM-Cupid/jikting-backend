package com.cupid.jikting.member.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsRequest {

    private String type;
    private String from;
    private String content;
    private List<SendSmsRequest> messages;
}
