package com.app.groupmissionapi.domain.auth.service;

import com.app.groupmissionapi.domain.auth.dto.request.LoginRequest;
import com.app.groupmissionapi.domain.auth.dto.response.LoginResponse;
import com.app.groupmissionapi.domain.member.entity.Member;
import com.app.groupmissionapi.domain.member.entity.MemberStatus;
import com.app.groupmissionapi.domain.member.repository.MemberRepository;
import com.app.groupmissionapi.global.constant.JwtConstants;
import com.app.groupmissionapi.global.exception.AuthException;
import com.app.groupmissionapi.global.exception.ErrorCode;
import com.app.groupmissionapi.global.security.jwt.JwtProvider;
import com.app.groupmissionapi.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CookieUtil cookieUtil;
  private final RefreshTokenService refreshTokenService;

  /** 로그인 */
  @Transactional(noRollbackFor = AuthException.class)
  public LoginResponse login(LoginRequest request, HttpServletResponse response) {

    LocalDateTime now = LocalDateTime.now();

    // 1. 아이디 조회
    Member member = memberRepository.findByEmail(request.getEmail())
      .orElseThrow(() -> new AuthException(ErrorCode.EMAIL_OR_PASSWORD_INVALID));

    // 2. 상태 확인
    if (member.getStatus() != MemberStatus.ACTIVE) {
      throw new AuthException(ErrorCode.MEMBER_LOCKED);
    }

    // 3. 비밀번호 오입력 횟수 확인
    if (member.isPasswordChangeRequired()) {
      throw new AuthException(ErrorCode.PASSWORD_CHANGE_REQUIRED);
    }

    // 4. 비밀번호 일치 여부 확인
    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {

      member.increaseLoginFailCount();

      // 비밀번호 변경 유도
      if (member.isPasswordChangeRequired()) {
        throw new AuthException(ErrorCode.PASSWORD_CHANGE_REQUIRED);
      }

      throw new AuthException(ErrorCode.EMAIL_OR_PASSWORD_INVALID);
    }

    // 5. 로그인 성공 처리
    member.successLogin(now);

    // 6. token 발급 및 쿠키 저장
    String accessToken = jwtProvider.generateAccessToken(member.getId(), member.getEmail());
    String refreshToken = jwtProvider.generateRefreshToken(member.getId());

    cookieUtil.addCookie(
      response,
      JwtConstants.ACCESS_TOKEN_COOKIE_NAME,
      accessToken,
      (int) (jwtProvider.getAccessTokenExpirationMs() / 1000)
    );

    cookieUtil.addCookie(
      response,
      JwtConstants.REFRESH_TOKEN_COOKIE_NAME,
      refreshToken,
      (int) (jwtProvider.getRefreshTokenExpirationMs() / 1000)
    );

    // 7. refresh token 저장
    refreshTokenService.save(
      member.getId(),
      refreshToken,
      jwtProvider.getRefreshTokenExpirationMs()
    );

    return new LoginResponse(
      member.getId(),
      member.getEmail(),
      member.getNickname()
    );
  }

}
