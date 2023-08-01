package com.cupid.jikting.member.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {

    @NotEmpty(message = "아이디는 빈칸일 수 없습니다.")
    @Pattern(regexp = "[a-z]{1}[a-z0-9]{5,19}")
    private String username;

    @NotEmpty(message = "비밀번호는 빈칸일 수 없습니다.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d!@#$%^&*]{10,}")
    private String password;

    @NotEmpty(message = "이름은 빈칸일 수 없습니다.")
    @Pattern(regexp = "[ㄱ-힣]{2,6}")
    private String name;

    @NotEmpty(message = "휴대폰 번호는 빈칸일 수 없습니다.")
    @Pattern(regexp = "[0-9]{11}")
    private String phone;

    @NotEmpty(message = "닉네임은 빈칸일 수 없습니다.")
    @Pattern(regexp = "[ㄱ-힣a-zA-Z]{2,15}")
    private String nickname;

    @NotNull
    private String gender;
}
