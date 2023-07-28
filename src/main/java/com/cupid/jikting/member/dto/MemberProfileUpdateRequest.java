package com.cupid.jikting.member.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileUpdateRequest {

    private LocalDate birth;
    private int height;
    private String mbti;
    private String address;
    private String gender;
    private String college;
    private String smokeStatus;
    private String drinkStatus;
    private String description;
    private List<String> personalities;
    private List<String> hobbies;
    private List<ImageResponse> images;
}
