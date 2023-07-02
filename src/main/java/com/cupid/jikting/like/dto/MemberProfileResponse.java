package com.cupid.jikting.like.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {

    private String nickname;
    private String image;
    private int age;
    private String mbti;
    private String address;
    private String company;
    private boolean isSmoke;
    private String drinkStatus;
    private int height;
    private String description;
    private List<String> keywords;
    private String college;
}
