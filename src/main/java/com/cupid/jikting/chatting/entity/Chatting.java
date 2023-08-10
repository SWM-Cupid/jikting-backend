package com.cupid.jikting.chatting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "chatting_id"))
public class Chatting extends BaseEntity implements Serializable {

    @Id
    private UUID id = UUID.randomUUID();

    private Long senderId;
    private String content;
}
