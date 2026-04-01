package com.app.daymoya.domain.auth.controller;

import com.app.daymoya.domain.auth.dto.request.*;
import com.app.daymoya.domain.auth.dto.response.MeResponse;
import com.app.daymoya.domain.auth.service.AuthService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentMemberId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.app.daymoya.global.constant.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;
import static com.app.daymoya.global.util.WebUtil.getClientIp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  /** 로그인 */
  @PostMapping("/public/login")
  public ApiResponse<Void> login(@Valid @RequestBody LoginRequest request
                                         ,HttpServletResponse httpResponse) {

    authService.login(request, httpResponse);
    return ApiResponse.success(null);
  }

  /** 회원가입 메일 인증코드 전송 */
  @PostMapping("/public/signup/code")
  public ApiResponse<Void> sendSignupCode(@Valid @RequestBody SignupCodeRequest request
                                         ,HttpServletRequest httpRequest) {

    String ip = getClientIp(httpRequest);
    authService.sendSignupCode(request.getEmail(), ip);
    return ApiResponse.success(null);
  }

  /** 회원가입 메일 인증코드 확인 */
  @PostMapping("/public/signup/code/verify")
  public ApiResponse<Void> verifySignupCode(@Valid @RequestBody VerifySignupCodeRequest request) {

    authService.verifySignupCode(request.getEmail(), request.getCode());
    return ApiResponse.success(null);
  }

  /** 회원가입 */
  @PostMapping(value = "/public/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<Void> signup(@Valid @ModelAttribute("request") SignupRequest request
                                 ,HttpServletRequest httpRequest) {

    String ip = getClientIp(httpRequest);
    authService.signup(request, ip);
    return ApiResponse.success(null);
  }

  /** 비밀번호 찾기 메일 인증코드 전송 */
  @PostMapping("/public/password/forgot/code")
  public ApiResponse<Void> sendPasswordForgotCode(@Valid @RequestBody PasswordForgotCodeRequest request) {

    authService.sendPasswordForgotCode(request.getEmail());
    return ApiResponse.success(null);
  }

  /** 비밀번호 찾기 메일 인증코드 확인 */
  @PostMapping("/public/password/forgot/code/verify")
  public ApiResponse<Void> verifyPasswordForgotCode(@Valid @RequestBody VerifyPasswordForgotCodeRequest request) {

    authService.verifyPasswordForgotCode(request.getEmail(), request.getCode());
    return ApiResponse.success(null);
  }

  /** 비밀번호 재설정 */
  @PostMapping("/public/password/forgot/reset")
  public ApiResponse<Void> resetForgottenPassword(@Valid @RequestBody PasswordForgotResetRequest request) {

    authService.resetForgottenPassword(request);
    return ApiResponse.success(null);
  }

  /** access token 재발급 */
  @PostMapping("/public/refresh/token")
  public ApiResponse<Void> refreshAccessToken(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken
                                             ,HttpServletResponse httpResponse) {

    authService.refreshAccessToken(refreshToken, httpResponse);
    return ApiResponse.success(null);
  }

  /** 현재 로그인 사용자 정보 조회 */
  @GetMapping("/me")
  public ApiResponse<MeResponse> me(@CurrentMemberId Long memberId) {

    MeResponse me = authService.getMe(memberId);
    return ApiResponse.success(me);
  }

}
