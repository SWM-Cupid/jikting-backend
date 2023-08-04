package com.cupid.jikting.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneVerificationRequest {

    private String phone;
    private String verificationCode;
}
