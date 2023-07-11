package com.cupid.jikting.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String nickname;
    private List<String> images;
    private int age;
    private String mbti;
    private String address;
}
