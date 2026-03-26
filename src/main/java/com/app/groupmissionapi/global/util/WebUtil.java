package com.app.groupmissionapi.global.util;

import jakarta.servlet.http.HttpServletRequest;

public final class WebUtil {

  private WebUtil() {}

  public static String getClientIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
      return ip.split(",")[0].trim();
    }

    ip = request.getHeader("X-Real-IP");
    if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
      return ip;
    }

    return request.getRemoteAddr();
  }

}
