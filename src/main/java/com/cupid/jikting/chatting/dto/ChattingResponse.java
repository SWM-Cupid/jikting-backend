package com.cupid.jikting.chatting.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingResponse {

    private Long chattingRoomId;
    private String opposingTeamName;
    private String lastMessage;
    private List<String> images;
}
