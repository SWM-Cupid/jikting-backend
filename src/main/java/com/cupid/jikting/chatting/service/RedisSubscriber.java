package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.common.util.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final JsonParser jsonParser;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ChattingRequest chattingRequest = jsonParser.toChattingRequest((String) redisTemplate.getStringSerializer().deserialize(message.getBody()));
        messagingTemplate.convertAndSend("/subscription/chattings/rooms/" + chattingRequest.getChattingRoomId(), chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRequest.getChattingRoomId());
    }
}
