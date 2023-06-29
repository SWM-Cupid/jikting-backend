package com.cupid.jikting.meeting.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamProfileResponse {

    private String name;
    private List<String> keywords;
    private List<String> imageUrls;
}
