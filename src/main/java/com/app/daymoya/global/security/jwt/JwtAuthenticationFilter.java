package com.app.daymoya.global.security.jwt;

import com.app.daymoya.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.app.daymoya.global.constant.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final CookieUtil cookieUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request
                                 ,HttpServletResponse response
                                 ,FilterChain filterChain) throws ServletException, IOException {

    String token = cookieUtil.getCookieValue(request, ACCESS_TOKEN_COOKIE_NAME);

    if (token != null && jwtProvider.validateAccessToken(token)) {
      Authentication authentication = jwtProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
