package com.cupid.jikting.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetVerificationCodeRequest {

    private String username;
    private String name;
    private String phone;
}
