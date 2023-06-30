package com.cupid.jikting.member.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {

    private List<ImageResponse> images;
    private int age;
    private int height;
    private String gender;
    private String address;
    private String mbti;
    private boolean isSmoke;
    private String drinkStatus;
    private String college;
    private List<String> personalities;
    private List<String> hobbies;
    private String description;
}
