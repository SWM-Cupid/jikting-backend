package com.cupid.jikting.chatting.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingResponse {

    private Long senderId;
    private String content;
    private String createdDate;
    private String createdTime;
}
