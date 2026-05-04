package com.app.daymoya.global.redis;

public final class RedisKeys {

  private RedisKeys() {}

  /** ======= AUTH 관련 ======= */
  public static final class Auth {

    private Auth() {}

    private static final String PREFIX = "auth:refresh:";

    public static String refreshToken(String tokenHash) {
      return PREFIX + tokenHash;
    }

    public static String userRefreshTokenSet(Long userId) {
      return PREFIX + "user:" + userId;
    }
  }

  /** ======= 회원가입 관련 ======= */
  public static final class Signup {

    private Signup() {}

    private static final String PREFIX = "auth:signup:";

    public static String code(String email) {
      return PREFIX + "code:" + email;
    }

    public static String failCount(String email) {
      return PREFIX + "fail:" + email;
    }

    public static String cooldown(String email) {
      return PREFIX + "cooldown:" + email;
    }

    public static String verified(String email) {
      return PREFIX + "verified:" + email;
    }

    public static String ipLimit(String ip) {
      return PREFIX + "ip:" + ip;
    }
  }

  /** ======= 비밀번호 초기화 관련 ======= */
  public static final class PasswordReset {

    private PasswordReset() {}

    private static final String PREFIX = "auth:password:reset:";

    public static String code(String email) {
      return PREFIX + "code:" + email;
    }

    public static String failCount(String email) {
      return PREFIX + "fail:" + email;
    }

    public static String cooldown(String email) {
      return PREFIX + "cooldown:" + email;
    }

    public static String verified(String email) {
      return PREFIX + "verified:" + email;
    }
  }

}
