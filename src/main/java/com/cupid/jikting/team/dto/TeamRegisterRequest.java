package com.cupid.jikting.team.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRegisterRequest {

    private String description;
    private int memberCount;
    private List<String> keywords;
}
