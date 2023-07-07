package com.cupid.jikting.chatting.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingResponse {

    Long chattingId;
    String opposingTeamName;
    String lastMessage;
}
