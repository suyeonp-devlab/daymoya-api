package com.app.daymoya.domain.auth.controller;

import com.app.daymoya.domain.auth.dto.request.*;
import com.app.daymoya.domain.auth.dto.response.MeResponse;
import com.app.daymoya.domain.auth.service.AuthService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.app.daymoya.global.security.jwt.JwtProvider.REFRESH_TOKEN_COOKIE_NAME;
import static com.app.daymoya.global.util.WebUtil.getClientIp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  /** 회원가입 메일 인증코드 전송 */
  @PostMapping("/public/signup/code")
  public ApiResponse<Void> sendSignupCode(@Valid @RequestBody CodeRequest request, HttpServletRequest httpRequest) {

    String ip = getClientIp(httpRequest);

    authService.sendSignupCode(request, ip);
    return ApiResponse.success();
  }

  /** 회원가입 메일 인증코드 확인 */
  @PostMapping("/public/signup/verify")
  public ApiResponse<Void> verifySignupCode(@Valid @RequestBody CodeVerifyRequest request) {
    authService.verifySignupCode(request);
    return ApiResponse.success();
  }

  /** 회원가입 */
  @PostMapping(value = "/public/signup")
  public ApiResponse<Void> signup(@Valid @RequestBody SignupRequest request, HttpServletRequest httpRequest) {

    String ip = getClientIp(httpRequest);

    authService.signup(request, ip);
    return ApiResponse.success();
  }

  /** 비밀번호 초기화 인증코드 전송 */
  @PostMapping("/public/password/reset/code")
  public ApiResponse<Void> sendPasswordResetCode(@Valid @RequestBody CodeRequest request) {
    authService.sendPasswordResetCode(request);
    return ApiResponse.success();
  }

  /** 비밀번호 초기화 인증코드 확인 */
  @PostMapping("/public/password/reset/verify")
  public ApiResponse<Void> verifyPasswordResetCode(@Valid @RequestBody CodeVerifyRequest request) {
    authService.verifyPasswordResetCode(request);
    return ApiResponse.success();
  }

  /** 비밀번호 초기화 */
  @PostMapping("/public/password/reset")
  public ApiResponse<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request, HttpServletResponse httpResponse) {
    authService.resetPassword(request, httpResponse);
    return ApiResponse.success();
  }

  /** 로그인 */
  @PostMapping("/public/login")
  public ApiResponse<String> login(@Valid @RequestBody LoginRequest request, HttpServletResponse httpResponse) {
    String token = authService.login(request, httpResponse);
    return ApiResponse.success(token);
  }

  /** accessToken 갱신 */
  @PostMapping("/public/refresh")
  public ApiResponse<String> refreshAccessToken(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
    String token = authService.refreshAccessToken(refreshToken);
    return ApiResponse.success(token);
  }

  /** 로그인 사용자 정보 조회 */
  @GetMapping("/me")
  public ApiResponse<MeResponse> getMe(@CurrentUserId Long userId) {
    MeResponse me = authService.getMe(userId);
    return ApiResponse.success(me);
  }

  /** 탈퇴 */
  @DeleteMapping("/me")
  public ApiResponse<Void> withdrawMe(
    @CurrentUserId Long userId,
    @Valid @RequestBody WithdrawRequest request,
    HttpServletResponse httpResponse
  ) {
    authService.withdrawMe(userId, request, httpResponse);
    return ApiResponse.success();
  }

  /** 로그아웃 */
  @PostMapping("/logout")
  public ApiResponse<Void> logout(@CurrentUserId Long userId, HttpServletResponse httpResponse) {
    authService.logout(userId, httpResponse);
    return ApiResponse.success();
  }

}
