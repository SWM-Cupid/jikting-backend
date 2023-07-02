package com.cupid.jikting.like.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDetailResponse {

    private Long likeId;
    private String teamName;
    private List<String> keywords;
    private List<MemberResponse> members;
}
