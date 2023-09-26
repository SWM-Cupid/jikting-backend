package com.cupid.jikting.member.dto;

import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordUpdateRequest {

    private String password;
    private String newPassword;
}
