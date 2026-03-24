package com.app.groupmissionapi.domain.auth.controller;

import com.app.groupmissionapi.domain.auth.dto.request.LoginRequest;
import com.app.groupmissionapi.domain.auth.dto.response.LoginResponse;
import com.app.groupmissionapi.domain.auth.service.AuthService;
import com.app.groupmissionapi.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request
                                         ,HttpServletResponse response) {

    LoginResponse result = authService.login(request, response);
    return ApiResponse.success(result);
  }

}
