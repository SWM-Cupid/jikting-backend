package com.cupid.jikting.member.dto;

import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetRequest {

    private String username;
    private String password;
}
