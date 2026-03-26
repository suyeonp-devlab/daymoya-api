package com.app.groupmissionapi.global.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.app.groupmissionapi.global.redis.RedisKeys.*;
import static com.app.groupmissionapi.global.redis.RedisTtl.*;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

  private final StringRedisTemplate stringRedisTemplate;

  public void saveSignupCode(String email, String code) {
    String key = signupCodeKey(email);
    stringRedisTemplate.opsForValue().set(key, code, SIGNUP_CODE_TTL);
  }

  public String findSignupCode(String email) {
    String key = signupCodeKey(email);
    return stringRedisTemplate.opsForValue().get(key);
  }

  public void deleteSignupCode(String email) {
    String key = signupCodeKey(email);
    stringRedisTemplate.delete(key);
  }

  public boolean isSignupCooldown(String email) {
    String key = signupCooldownKey(email);
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

  public void markSignupCooldown(String email) {
    String key = signupCooldownKey(email);
    stringRedisTemplate.opsForValue().set(key, "1", SIGNUP_COOLDOWN_TTL);
  }

  public void clearSignupFailCount(String email) {
    String key = signupFailCountKey(email);
    stringRedisTemplate.delete(key);
  }

  public boolean isSignupBlocked(String ip) {
    String key = signupIpLimitKey(ip);
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

}
