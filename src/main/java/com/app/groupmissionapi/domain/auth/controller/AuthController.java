package com.app.groupmissionapi.domain.auth.controller;

import com.app.groupmissionapi.domain.auth.dto.request.LoginRequest;
import com.app.groupmissionapi.domain.auth.dto.request.SignupCodeRequest;
import com.app.groupmissionapi.domain.auth.dto.response.LoginResponse;
import com.app.groupmissionapi.domain.auth.service.AuthService;
import com.app.groupmissionapi.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.app.groupmissionapi.global.util.WebUtil.getClientIp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request
                                         ,HttpServletResponse httpResponse) {

    LoginResponse result = authService.login(request, httpResponse);
    return ApiResponse.success(result);
  }

  @PostMapping("/signup/code")
  public ApiResponse<Void> sendSignupCode(@Valid @RequestBody SignupCodeRequest request
                                         ,HttpServletRequest httpRequest) {

    String ip = getClientIp(httpRequest);
    authService.sendSignupCode(request.getEmail(), ip);
    return ApiResponse.success(null);
  }


}
