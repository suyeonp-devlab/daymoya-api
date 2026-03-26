package com.app.groupmissionapi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.app.groupmissionapi.global.redis.RedisKeys.refreshTokenKey;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final StringRedisTemplate stringRedisTemplate;

  public void save(Long memberId, String refreshToken, long expirationMs) {
    String key = refreshTokenKey(memberId);
    stringRedisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMs));
  }

  public String find(Long memberId) {
    String key = refreshTokenKey(memberId);
    return stringRedisTemplate.opsForValue().get(key);
  }

  public void delete(Long memberId) {
    String key = refreshTokenKey(memberId);
    stringRedisTemplate.delete(key);
  }

}
