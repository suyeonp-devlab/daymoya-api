package com.app.daymoya.global.security;

import com.app.daymoya.global.exception.ErrorCode;
import com.app.daymoya.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request
                      ,HttpServletResponse response
                      ,AuthenticationException authException) throws IOException {

    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    response.setStatus(errorCode.getStatus().value());
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiResponse<Void> body = ApiResponse.fail(errorCode.getCode(), errorCode.getMessage());
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }

}
