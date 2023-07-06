package com.goodjob.core.global.base.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUt {

    @Autowired
    private RedisTemplate redisTemplate;

    // 리프레시 토큰 생성
    public String genRefreshToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public boolean hasValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(key);

        if (refreshToken == null) {
            return false;
        }

        return true;
    }

    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setValue(String key, String value, long timeout) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
