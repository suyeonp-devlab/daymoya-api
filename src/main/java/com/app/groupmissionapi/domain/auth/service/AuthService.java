package com.app.groupmissionapi.domain.auth.service;

import com.app.groupmissionapi.domain.auth.dto.request.LoginRequest;
import com.app.groupmissionapi.domain.auth.dto.response.LoginResponse;
import com.app.groupmissionapi.domain.member.entity.Member;
import com.app.groupmissionapi.domain.member.entity.MemberStatus;
import com.app.groupmissionapi.domain.member.repository.MemberRepository;
import com.app.groupmissionapi.global.constant.JwtConstants;
import com.app.groupmissionapi.global.exception.AuthException;
import com.app.groupmissionapi.global.exception.CustomException;
import com.app.groupmissionapi.global.mail.service.MailService;
import com.app.groupmissionapi.global.mail.service.MailVerificationService;
import com.app.groupmissionapi.global.security.jwt.JwtProvider;
import com.app.groupmissionapi.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.app.groupmissionapi.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CookieUtil cookieUtil;
  private final RefreshTokenService refreshTokenService;
  private final MailService mailService;
  private final MailVerificationService mailVerificationService;

  /** 로그인 */
  @Transactional(noRollbackFor = AuthException.class)
  public LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) {

    LocalDateTime now = LocalDateTime.now();

    // 이메일 조회
    Member member = memberRepository.findByEmail(request.getEmail())
      .orElseThrow(() -> new AuthException(EMAIL_OR_PASSWORD_INVALID));

    // 상태 확인
    if (member.getStatus() != MemberStatus.ACTIVE) {
      throw new AuthException(MEMBER_LOCKED);
    }

    // 비밀번호 오입력 횟수 확인
    if (member.isPasswordChangeRequired()) {
      throw new AuthException(PASSWORD_CHANGE_REQUIRED);
    }

    // 비밀번호 일치 여부 확인
    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {

      member.increaseLoginFailCount();

      // 비밀번호 변경 유도
      if (member.isPasswordChangeRequired()) {
        throw new AuthException(PASSWORD_CHANGE_REQUIRED);
      }

      throw new AuthException(EMAIL_OR_PASSWORD_INVALID);
    }

    // 로그인 성공 처리
    member.successLogin(now);

    // token 발급 및 쿠키 저장
    String accessToken = jwtProvider.generateAccessToken(member.getId(), member.getEmail());
    String refreshToken = jwtProvider.generateRefreshToken(member.getId());

    cookieUtil.addCookie(
      httpResponse,
      JwtConstants.ACCESS_TOKEN_COOKIE_NAME,
      accessToken,
      (int) (jwtProvider.getAccessTokenExpirationMs() / 1000)
    );

    cookieUtil.addCookie(
      httpResponse,
      JwtConstants.REFRESH_TOKEN_COOKIE_NAME,
      refreshToken,
      (int) (jwtProvider.getRefreshTokenExpirationMs() / 1000)
    );

    // refresh token 저장
    refreshTokenService.save(
      member.getId(),
      refreshToken,
      jwtProvider.getRefreshTokenExpirationMs()
    );

    return new LoginResponse(
      member.getId(),
      member.getNickname(),
      member.getProfileImageUrl()
    );
  }

  /** 회원가입 메일 인증코드 전송 */
  public void sendSignupCode(String email, String ip){

    // 가입여부
    if (memberRepository.existsByEmail(email)) {
      throw new CustomException(EMAIL_ALREADY_EXISTS);
    }

    // 메일 재전송 제한 - IP
    if (mailVerificationService.isSignupBlocked(ip)) {
      throw new CustomException(TOO_MANY_REQUEST);
    }

    // 메일 재전송 제한 - 쿨다운
    if (mailVerificationService.isSignupCooldown(email)) {
      throw new CustomException(TOO_MANY_REQUEST);
    }

    // 메일 전송 후 code 저장
    String code = mailService.sendMailCode(email);
    mailVerificationService.saveSignupCode(email, code);

    // 실패 횟수 초기화 & 메일 재전송 제한 설정
    mailVerificationService.clearSignupFailCount(email);
    mailVerificationService.markSignupCooldown(email);
  }

}
