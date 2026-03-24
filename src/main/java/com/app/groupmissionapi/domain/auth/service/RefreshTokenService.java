package com.app.groupmissionapi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.app.groupmissionapi.global.constant.JwtConstants.REFRESH_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final StringRedisTemplate stringRedisTemplate;

  public void save(Long memberId, String refreshToken, long expirationMs) {
    stringRedisTemplate.opsForValue().set(
      generateKey(memberId),
      refreshToken,
      Duration.ofMillis(expirationMs)
    );
  }

  public String find(Long memberId) {
    return stringRedisTemplate.opsForValue().get(generateKey(memberId));
  }

  public void delete(Long memberId) {
    stringRedisTemplate.delete(generateKey(memberId));
  }

  private String generateKey(Long memberId) {
    return REFRESH_TOKEN_PREFIX + memberId;
  }

}
