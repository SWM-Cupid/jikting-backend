package com.cupid.jikting.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendSmsRequest {

    private String to;

    public static SendSmsRequest from(String phone) {
        return new SendSmsRequest(phone);
    }
}
