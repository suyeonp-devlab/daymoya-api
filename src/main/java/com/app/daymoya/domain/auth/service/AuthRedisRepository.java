package com.app.daymoya.domain.auth.service;

import com.app.daymoya.global.redis.RedisKeys;
import com.app.daymoya.global.redis.RedisTtl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepository {

  private final StringRedisTemplate stringRedisTemplate;

  /** =====================================
   * 로그인 관련
   ========================================= */
  public void saveRefreshToken(String refreshTokenHash, Long userId, long expirationMs) {

    String tokenKey = RedisKeys.Auth.refreshToken(refreshTokenHash);
    stringRedisTemplate.opsForValue().set(tokenKey, String.valueOf(userId), Duration.ofMillis(expirationMs));

    // 멀티 로그인 고려
    String userKey = RedisKeys.Auth.userRefreshTokenSet(userId);
    stringRedisTemplate.opsForSet().add(userKey, refreshTokenHash);
    stringRedisTemplate.expire(userKey, Duration.ofMillis(expirationMs));
  }

  public Optional<Long> findUserIdByRefreshToken(String refreshTokenHash) {
    String tokenKey = RedisKeys.Auth.refreshToken(refreshTokenHash);
    String userId = stringRedisTemplate.opsForValue().get(tokenKey);
    return Optional.ofNullable(userId).map(Long::parseLong);
  }

  public void deleteAllRefreshTokenByUserId(Long userId) {

    String userKey = RedisKeys.Auth.userRefreshTokenSet(userId);
    Set<String> refreshTokens = stringRedisTemplate.opsForSet().members(userKey);

    // 멀티 로그인 전체 delete
    if(refreshTokens != null && !refreshTokens.isEmpty()) {
      List<String> keys = refreshTokens.stream().map(RedisKeys.Auth::refreshToken).toList();
      stringRedisTemplate.delete(keys);
    }

    stringRedisTemplate.delete(userKey);
  }

  /** =====================================
   * 회원가입 관련
   ========================================= */
  public void saveSignupCode(String email, String codeHash) {
    String key = RedisKeys.Signup.code(email);
    stringRedisTemplate.opsForValue().set(key, codeHash, RedisTtl.Signup.CODE);
  }

  public Optional<String> findSignupCode(String email) {
    String key = RedisKeys.Signup.code(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return Optional.ofNullable(value);
  }

  public void deleteSignupCode(String email) {
    String key = RedisKeys.Signup.code(email);
    stringRedisTemplate.delete(key);
  }

  public void saveSignupCooldown(String email) {
    String key = RedisKeys.Signup.cooldown(email);
    stringRedisTemplate.opsForValue().set(key, "1", RedisTtl.Signup.COOLDOWN);
  }

  public boolean hasSignupCooldown(String email) {
    String key = RedisKeys.Signup.cooldown(email);
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

  public void deleteSignupCooldown(String email) {
    String key = RedisKeys.Signup.cooldown(email);
    stringRedisTemplate.delete(key);
  }

  public void increaseSignupFailCount(String email) {

    String key = RedisKeys.Signup.failCount(email);
    Long count = stringRedisTemplate.opsForValue().increment(key);

    // 최초 생성인 경우 TTL 설정
    if (count != null && count == 1L) {
      stringRedisTemplate.expire(key, RedisTtl.Signup.FAIL_COUNT);
    }
  }

  public int findSignupFailCount(String email) {
    String key = RedisKeys.Signup.failCount(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return (value != null) ? Integer.parseInt(value) : 0;
  }

  public void deleteSignupFailCount(String email) {
    String key = RedisKeys.Signup.failCount(email);
    stringRedisTemplate.delete(key);
  }

  public void saveSignupIpLimit(String ip) {
    String key = RedisKeys.Signup.ipLimit(ip);
    stringRedisTemplate.opsForValue().set(key, "1", RedisTtl.Signup.IP_LIMIT);
  }

  public boolean hasSignupIpLimit(String ip) {
    String key = RedisKeys.Signup.ipLimit(ip);
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

  public void saveSignupVerified(String email, String codeHash) {
    String key = RedisKeys.Signup.verified(email);
    stringRedisTemplate.opsForValue().set(key, codeHash, RedisTtl.Signup.VERIFIED);
  }

  public Optional<String> findSignupVerified(String email) {
    String key = RedisKeys.Signup.verified(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return Optional.ofNullable(value);
  }

  public void deleteSignupVerified(String email) {
    String key = RedisKeys.Signup.verified(email);
    stringRedisTemplate.delete(key);
  }

  /** =====================================
   * 비밀번호 초기화 관련
   ========================================= */
  public void savePasswordResetCode(String email, String codeHash) {
    String key = RedisKeys.PasswordReset.code(email);
    stringRedisTemplate.opsForValue().set(key, codeHash, RedisTtl.PasswordReset.CODE);
  }

  public Optional<String> findPasswordResetCode(String email) {
    String key = RedisKeys.PasswordReset.code(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return Optional.ofNullable(value);
  }

  public void deletePasswordResetCode(String email) {
    String key = RedisKeys.PasswordReset.code(email);
    stringRedisTemplate.delete(key);
  }

  public void savePasswordResetCooldown(String email) {
    String key = RedisKeys.PasswordReset.cooldown(email);
    stringRedisTemplate.opsForValue().set(key, "1", RedisTtl.PasswordReset.COOLDOWN);
  }

  public boolean hasPasswordResetCooldown(String email) {
    String key = RedisKeys.PasswordReset.cooldown(email);
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

  public void deletePasswordResetCooldown(String email) {
    String key = RedisKeys.PasswordReset.cooldown(email);
    stringRedisTemplate.delete(key);
  }

  public void increasePasswordResetFailCount(String email) {

    String key = RedisKeys.PasswordReset.failCount(email);
    Long count = stringRedisTemplate.opsForValue().increment(key);

    // 최초 생성인 경우 TTL 설정
    if (count != null && count == 1L) {
      stringRedisTemplate.expire(key, RedisTtl.PasswordReset.FAIL_COUNT);
    }
  }

  public int findPasswordResetFailCount(String email) {
    String key = RedisKeys.PasswordReset.failCount(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return (value != null) ? Integer.parseInt(value) : 0;
  }

  public void deletePasswordResetFailCount(String email) {
    String key = RedisKeys.PasswordReset.failCount(email);
    stringRedisTemplate.delete(key);
  }

  public void savePasswordResetVerified(String email, String codeHash) {
    String key = RedisKeys.PasswordReset.verified(email);
    stringRedisTemplate.opsForValue().set(key, codeHash, RedisTtl.PasswordReset.VERIFIED);
  }

  public Optional<String> findPasswordResetVerified(String email) {
    String key = RedisKeys.PasswordReset.verified(email);
    String value = stringRedisTemplate.opsForValue().get(key);
    return Optional.ofNullable(value);
  }

  public void deletePasswordResetVerified(String email) {
    String key = RedisKeys.PasswordReset.verified(email);
    stringRedisTemplate.delete(key);
  }

}
