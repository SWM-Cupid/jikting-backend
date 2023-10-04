package com.cupid.jikting.member.dto;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendSmsRequest {

    private String to;

    public static SendSmsRequest from(String phone) {
        return new SendSmsRequest(phone);
    }
}
