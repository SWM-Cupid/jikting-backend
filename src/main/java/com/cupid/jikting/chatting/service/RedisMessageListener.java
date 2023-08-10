package com.cupid.jikting.chatting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisMessageListener {

    private static final Map<Long, ChannelTopic> TOPICS = new HashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;

    public void enterChattingRoom(Long chattingRoomId) {
        ChannelTopic topic = getTopic(chattingRoomId);
        if (topic == null) {
            topic = new ChannelTopic(String.valueOf(chattingRoomId));
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            TOPICS.put(chattingRoomId, topic);
        }
        log.info("enter in chatting room '{}'", chattingRoomId);
    }

    public ChannelTopic getTopic(Long chattingRoomId) {
        return TOPICS.get(chattingRoomId);
    }
}
