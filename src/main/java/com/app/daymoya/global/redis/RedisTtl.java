package com.app.daymoya.global.redis;

import java.time.Duration;

public final class RedisTtl {

  private RedisTtl() {}

  /** =====================================
   * 회원가입 관련
   ========================================= */
  public static final class Signup {

    private Signup() {}

    public static final Duration CODE = Duration.ofMinutes(5);
    public static final Duration FAIL_COUNT = Duration.ofMinutes(5);
    public static final Duration COOLDOWN = Duration.ofSeconds(60);
    public static final Duration VERIFIED = Duration.ofMinutes(30);
    public static final Duration IP_LIMIT = Duration.ofSeconds(10);
  }

  /** =====================================
   * 비밀번호 초기화 관련
   ========================================= */
  public static final class PasswordReset {

    private PasswordReset() {}

    public static final Duration CODE = Duration.ofMinutes(5);
    public static final Duration FAIL_COUNT = Duration.ofMinutes(5);
    public static final Duration COOLDOWN = Duration.ofSeconds(60);
    public static final Duration VERIFIED = Duration.ofMinutes(10);
  }

}