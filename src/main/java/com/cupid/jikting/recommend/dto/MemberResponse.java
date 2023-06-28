package com.cupid.jikting.recommend.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String nickname;
    private List<ImageResponse> images;
    private int age;
    private String mbti;
    private String address;
    private String company;
    private boolean isSmoke;
    private String drinkStatus;
    private int height;
    private String description;
    private List<String> personalities;
    private List<String> hobbies;
    private String college;
}
