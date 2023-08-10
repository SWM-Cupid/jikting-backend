package com.cupid.jikting.common.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.util.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisConnector {

    private static final String NO_MESSAGE = "";

    private final RedisTemplate<String, Object> redisTemplate;
    private final JsonParser jsonParser;

    public void set(String key, String value, int expireTime) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, Duration.ofMinutes(expireTime));
    }

    public String get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElseThrow(() -> new BadRequestException(ApplicationError.VERIFICATION_CODE_EXPIRED));
    }

    public void saveChatting(String chattingRoom, Chatting chatting) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(chattingRoom, chatting.getId(), chatting);
        log.info("create new chatting [{}] int chatting room '{}'", chatting, chattingRoom);
    }

    public String getLastMessage(String chattingRoom) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(chattingRoom)
                .stream()
                .map(String::valueOf)
                .map(jsonParser::toChattingRequest)
                .findFirst()
                .map(ChattingRequest::getContent)
                .orElse(NO_MESSAGE);
    }
}
