package com.cupid.jikting.common.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisJwtRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value, Duration expireTime) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime);
    }

    public boolean existAccessToken(String key) {
        return redisTemplate.opsForValue().get(key) != null;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
