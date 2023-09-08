package com.cupid.jikting.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class JwtRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> tokens;

    @PostConstruct
    private void init() {
        tokens = redisTemplate.opsForValue();
    }

    public void save(String key, String value, Duration expireTime) {
        ValueOperations<String, Object> valueOperations = tokens;
        valueOperations.set(key, value, expireTime);
    }

    public boolean existBy(String key) {
        return tokens.get(key) != null;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
