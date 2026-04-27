package com.app.daymoya.domain.auth.repository.redis;

import com.app.daymoya.global.redis.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.app.daymoya.global.redis.RedisKeys.*;
import static com.app.daymoya.global.redis.RedisKeys.refreshTokenKey;

@Service
@RequiredArgsConstructor
public class RefreshTokenRepository {

  private final StringRedisTemplate stringRedisTemplate;

  // 멀티 로그인 고려
  public void saveToken(String refreshTokenHash, Long userId, long expirationMs) {

    String tokenKey = refreshTokenKey(refreshTokenHash);
    stringRedisTemplate.opsForValue().set(tokenKey, String.valueOf(userId), Duration.ofMillis(expirationMs));

    String userKey = userRefreshTokenSetKey(userId);
    stringRedisTemplate.opsForSet().add(userKey, refreshTokenHash);
    stringRedisTemplate.expire(userKey, Duration.ofMillis(expirationMs));
  }

  public Optional<Long> findUserIdByToken(String refreshTokenHash) {
    String tokenKey = refreshTokenKey(refreshTokenHash);
    String userId = stringRedisTemplate.opsForValue().get(tokenKey);
    return Optional.ofNullable(userId).map(Long::parseLong);
  }

  // 사용자 모든 세션 종료
  public void deleteAllByUserId(Long userId) {

    String userKey = userRefreshTokenSetKey(userId);
    Set<String> tokens = stringRedisTemplate.opsForSet().members(userKey);

    if(tokens != null && !tokens.isEmpty()) {
      List<String> keys = tokens.stream().map(RedisKeys::refreshTokenKey).toList();
      stringRedisTemplate.delete(keys);
    }

    stringRedisTemplate.delete(userKey);
  }

}
