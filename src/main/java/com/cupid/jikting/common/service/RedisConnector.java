package com.cupid.jikting.common.service;

import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisConnector {

    private static final String NO_MESSAGE = "";

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value, Duration expireTime) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime);
    }

    public String get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElseThrow(() -> new BadRequestException(ApplicationError.VERIFICATION_CODE_EXPIRED));
    }

    public void saveChatting(String chattingRoom, Chatting chatting) {
        HashOperations<String, String, Chatting> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(chattingRoom, chatting.getId(), chatting);
        log.info("create new chatting [{}] int chatting room '{}'", chatting, chattingRoom);
    }

    public String getLastMessage(String chattingRoom) {
        HashOperations<String, String, Chatting> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(chattingRoom)
                .stream()
                .max(Comparator.comparing(Chatting::getCreatedAt))
                .map(Chatting::getContent)
                .orElse(NO_MESSAGE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public List<Chatting> getMessages(String chattingRoomKey) {
        HashOperations<String, String, Chatting> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(chattingRoomKey);
    }
}
