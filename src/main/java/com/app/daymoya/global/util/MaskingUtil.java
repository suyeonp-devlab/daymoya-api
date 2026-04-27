package com.app.daymoya.global.util;

import org.springframework.util.StringUtils;

public final class MaskingUtil {

  private MaskingUtil() {}

  /** 이메일 마스킹 */
  public static String maskEmail(String email) {

    // test@example.com → te**@example.com
    // abcdef@example.com → ab****@example.com
    // ab@example.com → a*@example.com

    if (!StringUtils.hasText(email) || !email.contains("@")) {
      return email;
    }

    String[] parts = email.split("@");
    String local = parts[0];
    String domain = parts[1];

    if (local.length() <= 2) {
      return local.charAt(0) + "*@" + domain;
    }

    String maskedLocal = local.substring(0, 2) + "*".repeat(local.length() - 2);
    return maskedLocal + "@" + domain;
  }

}
