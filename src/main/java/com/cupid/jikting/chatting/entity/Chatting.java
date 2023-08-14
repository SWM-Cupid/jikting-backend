package com.cupid.jikting.chatting.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "chatting_id"))
public class Chatting implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    private Long senderId;
    private String content;
}
