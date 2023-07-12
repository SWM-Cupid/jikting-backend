package com.cupid.jikting.chatting.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomDetailResponse {

    private String description;
    private List<String> keywords;
    private List<MemberResponse> members;
}
