package com.safeking.shop.global.jwt;

import com.safeking.shop.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRepository {

    private static final String BLACKLIST_PREFIX = "BLACKLIST_";
    private static final String UID_PREFIX = "UID_";

    private final RedisRepository redisRepository;

    public void addBlackList(String accessToken, Duration duration) {
        redisRepository.set(BLACKLIST_PREFIX + accessToken, accessToken, duration);
    }

    public void addRefreshToken(Long userId, String refreshToken, Duration duration) {
        redisRepository.set(UID_PREFIX + userId, refreshToken, duration);
    }

    public Optional<String> findBlackListTokenByToken(String accessToken) {
        return Optional.ofNullable(redisRepository.get(BLACKLIST_PREFIX + accessToken));
    }

    public Optional<String> findRefreshTokenByUserId(String userId) {
        return Optional.ofNullable(redisRepository.get(UID_PREFIX + userId));
    }

    public void removeRefreshToken(String userId) {
        redisRepository.remove(UID_PREFIX + userId);
    }
}
