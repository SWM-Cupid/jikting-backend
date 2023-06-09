package com.cupid.jikting.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {

    private String username;
    private String password;
    private String name;
    private String phone;
}
