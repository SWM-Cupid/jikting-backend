package com.cupid.jikting.chatting.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRequest {

    private Long chattingRoomId;
    private Long senderId;
    private String content;
}
