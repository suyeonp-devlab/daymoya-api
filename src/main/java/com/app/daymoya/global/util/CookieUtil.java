package com.app.daymoya.global.util;

import com.app.daymoya.global.properties.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieUtil {

  private final CookieProperties cookieProperties;

  private static final String COOKIE_PATH = "/";

  /** 쿠키 저장 */
  public void addCookie(HttpServletResponse response, String name, String value, long maxAgeSeconds) {

    ResponseCookie cookie = ResponseCookie.from(name, value)
      .httpOnly(true)
      .secure(cookieProperties.isSecure())
      .path(COOKIE_PATH)
      .maxAge(maxAgeSeconds)
      .sameSite(cookieProperties.getSameSite())
      .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  /** 쿠키 삭제 */
  public void deleteCookie(HttpServletResponse response, String name) {

    ResponseCookie cookie = ResponseCookie.from(name, "")
      .httpOnly(true)
      .secure(cookieProperties.isSecure())
      .path(COOKIE_PATH)
      .maxAge(0)
      .sameSite(cookieProperties.getSameSite())
      .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  /** 쿠키 조회 */
  public Optional<String> getCookieValue(HttpServletRequest request, String name) {

    Cookie[] cookies = request.getCookies();

    if (cookies == null) return Optional.empty();

    return Arrays.stream(cookies)
      .filter(c -> name.equals(c.getName()))
      .map(Cookie::getValue)
      .findFirst();
  }

}
