package br.com.microservices.auth.service.impl;

import br.com.microservices.auth.service.ITokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService implements ITokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "blacklist:token:";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void blacklistToken(String token, long expirationTimeMillis) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(key, "revoked", expirationTimeMillis, TimeUnit.MILLISECONDS);
            log.info("Token added to blacklist with TTL: {} ms", expirationTimeMillis);
        } catch (Exception e) {
            log.error("Error adding token to blacklist", e);
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

    @Override
    public boolean isBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Error checking token blacklist status", e);
            return false;
        }
    }

    @Override
    public void removeFromBlacklist(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.delete(key);
            log.info("Token removed from blacklist");
        } catch (Exception e) {
            log.error("Error removing token from blacklist", e);
        }
    }
}

