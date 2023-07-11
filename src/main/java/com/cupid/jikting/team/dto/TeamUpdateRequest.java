package com.cupid.jikting.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUpdateRequest {

    private String description;
    private List<String> keywords;
}
