package com.cupid.jikting.chatting.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatting implements Serializable {

    @Id
    private Long id;

    private Long senderId;
    private String content;
    private LocalDateTime createdAt;
}
