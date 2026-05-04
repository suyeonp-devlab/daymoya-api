package com.app.daymoya.domain.auth.service;

import com.app.daymoya.domain.auth.dto.request.*;
import com.app.daymoya.domain.auth.dto.response.MeResponse;
import com.app.daymoya.domain.users.entity.User;
import com.app.daymoya.domain.users.entity.UserStatus;
import com.app.daymoya.domain.users.repository.UserRepository;
import com.app.daymoya.global.exception.BizException;
import com.app.daymoya.global.file.FileService;
import com.app.daymoya.global.mail.MailService;
import com.app.daymoya.global.security.hash.Sha256Hash;
import com.app.daymoya.global.security.jwt.JwtProvider;
import com.app.daymoya.global.util.CookieUtil;
import com.app.daymoya.global.util.MaskingUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.app.daymoya.global.exception.ErrorCode.*;
import static com.app.daymoya.global.exception.ErrorCode.REFRESH_TOKEN_INVALID;
import static com.app.daymoya.global.security.jwt.JwtProvider.ACCESS_TOKEN_COOKIE_NAME;
import static com.app.daymoya.global.security.jwt.JwtProvider.REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CookieUtil cookieUtil;
  private final Sha256Hash sha256Hash;
  private final MailService mailService;
  private final UserRepository userRepository;
  private final FileService fileService;
  private final AuthRedisRepository authRedisRepository;

  private static final int PASSWORD_FAIL_CNT = 5;
  private static final int CODE_FAIL_CNT = 5;

  private static final List<String> DEFAULT_PROFILE_IMAGES = List.of(
    "profile/default-profile(1).png",
    "profile/default-profile(2).png",
    "profile/default-profile(3).png",
    "profile/default-profile(4).png"
  );

  /** 회원가입 메일 인증코드 전송 */
  public void sendSignupCode(CodeRequest request, String ip) {

    String email = request.getEmail().trim().toLowerCase();

    // 가입여부
    if (userRepository.existsByEmail(email)) {
      throw new BizException(EMAIL_ALREADY_EXISTS);
    }

    // 메일 재전송 제한 - IP
    if (authRedisRepository.hasSignupIpLimit(ip)) {
      throw new BizException(MANY_REQUEST);
    }

    // 메일 재전송 제한 - 쿨다운
    if (authRedisRepository.hasSignupCooldown(email)) {
      throw new BizException(MANY_REQUEST);
    }

    // 메일 전송 후 code 저장
    String code = mailService.sendMailCode(email);
    String codeHash = sha256Hash.hash(code);
    authRedisRepository.saveSignupCode(email, codeHash);

    log.info("sendSignupCode={}", code);

    // 실패 횟수 초기화 & 메일 재전송 제한 설정
    authRedisRepository.deleteSignupFailCount(email);
    authRedisRepository.saveSignupCooldown(email);
  }

  /** 회원가입 메일 인증코드 확인 */
  public void verifySignupCode(CodeVerifyRequest request) {

    String email = request.getEmail().trim().toLowerCase();
    String code = request.getCode();

    // 인증 실패 횟수 초과 여부
    int failCount = authRedisRepository.findSignupFailCount(email);
    if (failCount >= CODE_FAIL_CNT) {
      throw new BizException(MANY_VERIFICATION_ATTEMPTS);
    }

    // 저장된 인증코드 조회
    String savedCode = authRedisRepository.findSignupCode(email).orElse(null);
    if (savedCode == null) {
      throw new BizException(AUTH_CODE_EXPIRED);
    }

    // 인증코드 불일치
    if (!sha256Hash.matches(code, savedCode)) {
      authRedisRepository.increaseSignupFailCount(email);
      throw new BizException(AUTH_CODE_INVALID);
    }

    // 인증 성공 처리
    authRedisRepository.deleteSignupCode(email);
    authRedisRepository.deleteSignupFailCount(email);
    authRedisRepository.saveSignupVerified(email, savedCode);
  }

  /** 회원가입 */
  @Transactional
  public void signup(SignupRequest request, String ip) {

    String email = request.getEmail().trim().toLowerCase();
    LocalDateTime now = LocalDateTime.now();

    // 가입여부
    if (userRepository.existsByEmail(email)) {
      throw new BizException(EMAIL_ALREADY_EXISTS);
    }

    // 비밀번호 일치 여부
    if(!request.isPasswordMatched()) {
      throw new BizException(PASSWORD_CONFIRM_NOT_MATCH);
    }

    // 이메일 인증 완료 여부
    String verifiedCode = authRedisRepository.findSignupVerified(email).orElse(null);
    if (verifiedCode == null) {
      throw new BizException(EMAIL_NOT_VERIFIED);
    }

    // 랜덤 프로필 이미지
    String profileImagePath = getRandomProfileImagePath();

    // 회원가입
    User user = User.create(
      email,
      passwordEncoder.encode(request.getPassword()),
      request.getNickname(),
      profileImagePath,
      now
    );

    userRepository.save(user);

    // 회원가입 성공 처리
    authRedisRepository.deleteSignupVerified(email);
    authRedisRepository.saveSignupIpLimit(ip);
  }

  /** 비밀번호 초기화 인증코드 전송 */
  public void sendPasswordResetCode(CodeRequest request) {

    String email = request.getEmail().trim().toLowerCase();

    // 가입여부
    if (!userRepository.existsByEmail(email)) {
      throw new BizException(USER_NOT_FOUND);
    }

    // 메일 재전송 제한 - 쿨다운
    if (authRedisRepository.hasPasswordResetCooldown(email)) {
      throw new BizException(MANY_REQUEST);
    }

    // 메일 전송 후 code 저장
    String code = mailService.sendMailCode(email);
    String codeHash = sha256Hash.hash(code);
    authRedisRepository.savePasswordResetCode(email, codeHash);

    log.info("sendPasswordResetCode={}", code);

    // 실패 횟수 초기화 & 메일 재전송 제한 설정
    authRedisRepository.deletePasswordResetFailCount(email);
    authRedisRepository.savePasswordResetCooldown(email);
  }

  /** 비밀번호 초기화 인증코드 확인 */
  public void verifyPasswordResetCode(CodeVerifyRequest request) {

    String email = request.getEmail().trim().toLowerCase();
    String code = request.getCode();

    // 인증 실패 횟수 초과 여부
    int failCount = authRedisRepository.findPasswordResetFailCount(email);
    if (failCount >= CODE_FAIL_CNT) {
      throw new BizException(MANY_VERIFICATION_ATTEMPTS);
    }

    // 저장된 인증코드 조회
    String savedCode = authRedisRepository.findPasswordResetCode(email).orElse(null);
    if (savedCode == null) {
      throw new BizException(AUTH_CODE_EXPIRED);
    }

    // 인증코드 불일치
    if (!sha256Hash.matches(code, savedCode)) {
      authRedisRepository.increasePasswordResetFailCount(email);
      throw new BizException(AUTH_CODE_INVALID);
    }

    // 인증 성공 처리
    authRedisRepository.deletePasswordResetCode(email);
    authRedisRepository.deletePasswordResetFailCount(email);
    authRedisRepository.savePasswordResetVerified(email, savedCode);
  }

  /** 비밀번호 초기화 */
  @Transactional
  public void resetPassword(PasswordResetRequest request, HttpServletResponse httpResponse) {

    String email = request.getEmail().trim().toLowerCase();
    LocalDateTime now = LocalDateTime.now();

    // 회원 조회
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new BizException(USER_NOT_FOUND));

    // 비밀번호 일치 여부
    if(!request.isPasswordMatched()) {
      throw new BizException(PASSWORD_CONFIRM_NOT_MATCH);
    }

    // 이메일 인증 완료 여부
    String verifiedCode = authRedisRepository.findPasswordResetVerified(email).orElse(null);
    if (verifiedCode == null) {
      throw new BizException(EMAIL_NOT_VERIFIED);
    }

    // 이전 비밀번호와 일치 여부
    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BizException(PASSWORD_SAME_AS_OLD);
    }

    // 비밀번호 변경
    user.changePassword(passwordEncoder.encode(request.getPassword()), now);

    // 비밀번호 변경 성공 처리
    cookieUtil.deleteCookie(httpResponse, ACCESS_TOKEN_COOKIE_NAME);
    cookieUtil.deleteCookie(httpResponse, REFRESH_TOKEN_COOKIE_NAME);
    authRedisRepository.deletePasswordResetVerified(email);

    // 멀티 로그인 전체 delete
    authRedisRepository.deleteAllRefreshTokenByUserId(user.getId());
  }

  /** 로그인 */
  @Transactional(noRollbackFor = BizException.class)
  public void login(LoginRequest request, HttpServletResponse httpResponse) {

    String email = request.getEmail().trim().toLowerCase();
    LocalDateTime now = LocalDateTime.now();

    // 회원 조회
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new BizException(EMAIL_OR_PASSWORD_INVALID));

    // 상태 확인
    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new BizException(USER_LOCKED);
    }

    // 비밀번호 오입력 횟수
    if (user.getFailedLoginCount() >= PASSWORD_FAIL_CNT) {
      throw new BizException(PASSWORD_CHANGE_REQUIRED);
    }

    // 비밀번호 일치 여부
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      user.increaseFailedLoginCount();
      throw new BizException(EMAIL_OR_PASSWORD_INVALID);
    }

    // 로그인 성공 처리
    user.login(now);

    // token 발급 및 쿠키 저장
    String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole().name());
    String refreshToken = jwtProvider.generateRefreshToken(user.getId());
    String refreshTokenHash = sha256Hash.hash(refreshToken);

    cookieUtil.addCookie(
      httpResponse,
      ACCESS_TOKEN_COOKIE_NAME,
      accessToken,
      (int) (jwtProvider.getAccessTokenExpirationMs() / 1000)
    );

    cookieUtil.addCookie(
      httpResponse,
      REFRESH_TOKEN_COOKIE_NAME,
      refreshToken,
      (int) (jwtProvider.getRefreshTokenExpirationMs() / 1000)
    );

    authRedisRepository.saveRefreshToken(
      refreshTokenHash,
      user.getId(),
      jwtProvider.getRefreshTokenExpirationMs()
    );
  }

  /** accessToken 갱신 */
  public void refreshAccessToken(String refreshToken, HttpServletResponse httpResponse) {

    // refresh token 검증
    if (!StringUtils.hasText(refreshToken)) {
      throw new BizException(INVALID_VALUE);
    }

    if (!jwtProvider.validateRefreshToken(refreshToken)) {
      throw new BizException(REFRESH_TOKEN_INVALID);
    }

    // refresh token 사용자 ID 검증
    Long userId = jwtProvider.getUserId(refreshToken);
    String refreshTokenHash = sha256Hash.hash(refreshToken);

    Long savedUserId = authRedisRepository.findUserIdByRefreshToken(refreshTokenHash).orElse(null);
    if (savedUserId == null || !savedUserId.equals(userId)) {
      throw new BizException(REFRESH_TOKEN_INVALID);
    }

    // 회원 조회
    User user = userRepository.findById(savedUserId)
      .orElseThrow(() -> new BizException(USER_NOT_FOUND));

    // token 발급 및 쿠키 저장
    String newAccessToken = jwtProvider.generateAccessToken(savedUserId, user.getRole().name());

    cookieUtil.addCookie(
      httpResponse,
      ACCESS_TOKEN_COOKIE_NAME,
      newAccessToken,
      (int) (jwtProvider.getAccessTokenExpirationMs() / 1000)
    );
  }

  /** 로그인 사용자 정보 조회 */
  public MeResponse getMe(Long userId) {

    User user = userRepository.findById(userId)
      .orElseThrow(() -> new BizException(USER_NOT_FOUND));

    return MeResponse.builder()
      .userId(user.getId())
      .maskedEmail(MaskingUtil.maskEmail(user.getEmail()))
      .nickname(user.getNickname())
      .profileImageUrl(fileService.buildFileUrl(user.getProfileImagePath()))
      .build();
  }

  /** 탈퇴 */
  @Transactional
  public void withdrawMe(Long userId, WithdrawRequest request, HttpServletResponse httpResponse) {

    LocalDateTime now = LocalDateTime.now();

    // 회원 조회
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new BizException(USER_NOT_FOUND));

    // 상태 확인
    if (user.getStatus() == UserStatus.WITHDRAWN) {
      throw new BizException(USER_ALREADY_WITHDRAWN);
    }

    // 비밀번호 일치 여부
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BizException(PASSWORD_NOT_MATCH);
    }

    // 회원 탈퇴 처리
    user.withdraw(now);
    this.logout(userId, httpResponse);
  }

  /** 로그아웃 */
  public void logout(Long userId, HttpServletResponse httpResponse) {

    // 쿠키 삭제
    cookieUtil.deleteCookie(httpResponse, ACCESS_TOKEN_COOKIE_NAME);
    cookieUtil.deleteCookie(httpResponse, REFRESH_TOKEN_COOKIE_NAME);

    // 멀티 로그인 전체 delete
    authRedisRepository.deleteAllRefreshTokenByUserId(userId);
  }

  /** 랜덤 프로필 선택 */
  private String getRandomProfileImagePath() {
    int randomIndex = ThreadLocalRandom.current().nextInt(DEFAULT_PROFILE_IMAGES.size());
    return DEFAULT_PROFILE_IMAGES.get(randomIndex);
  }

}
