package com.cupid.jikting.chatting.dto;

import com.cupid.jikting.chatting.entity.Chatting;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRequest {

    private Long chattingRoomId;
    private Long senderId;
    private String content;

    public Chatting toChatting() {
        return Chatting.builder()
                .senderId(senderId)
                .content(content)
                .build();
    }
}
