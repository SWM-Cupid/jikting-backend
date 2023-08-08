package com.cupid.jikting.common.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RedisConnector {

    private final RedisTemplate<String, String> redisTemplate;

    public void set(String key, String value, Duration expireTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime);
    }

    public String get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new BadRequestException(ApplicationError.VERIFICATION_CODE_EXPIRED));
    }

    public String getUsernameByRefreshToken(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new JwtException(ApplicationError.EXPIRED_TOKEN));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
