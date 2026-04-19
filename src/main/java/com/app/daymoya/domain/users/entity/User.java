package com.app.daymoya.domain.users.entity;

import com.app.daymoya.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {

  private static final int PASSWORD_FAIL_CNT = 5;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 이메일
  @Column(nullable = false, unique = true)
  private String email;

  // 비밀번호
  @Column(nullable = false)
  private String password;

  // 닉네임
  @Column(nullable = false, length = 50)
  private String nickname;

  // 프로필 이미지 경로
  @Column(length = 500)
  private String profileImagePath;

  // 사용자 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserStatus status;

  // 연속 로그인 실패 횟수
  @Column(nullable = false)
  private int failedLoginCount;

  // 마지막 비밀번호 변경 시각
  private LocalDateTime passwordChangedAt;

  // 마지막 로그인 시간
  private LocalDateTime lastLoginAt;

  // 탈퇴 시각
  private LocalDateTime deletedAt;

  /** 비밀번호 실패 처리 */
  public void increaseFailedLoginCount() {
    this.failedLoginCount++;
  }

  /** 비밀번호 변경 강제 여부 */
  public boolean isPasswordChangeRequired() {
    return this.failedLoginCount >= PASSWORD_FAIL_CNT;
  }

  /** 로그인 성공 처리 */
  public void successLogin(LocalDateTime now) {
    this.failedLoginCount = 0;
    this.lastLoginAt = now;
  }

  /** 회원 생성 */
  public static User create(
     String email
    ,String encodedPassword
    ,String nickname
    ,String profileImagePath
    ,LocalDateTime now
  ) {

    return User.builder()
      .email(email)
      .password(encodedPassword)
      .nickname(nickname)
      .profileImagePath(profileImagePath)
      .status(UserStatus.ACTIVE)
      .failedLoginCount(0)
      .passwordChangedAt(now)
      .build();
  }

  /** 비밀번호 변경 */
  public void changePassword(String encodedPassword, LocalDateTime now) {
    this.password = encodedPassword;
    this.failedLoginCount = 0;
    this.passwordChangedAt = now;
  }

  /** 회원 탈퇴 */
  public void withdrawUser(String maskedEmail, LocalDateTime now) {
    this.email = maskedEmail;
    this.nickname = "탈퇴회원";
    this.profileImagePath = null;
    this.status = UserStatus.WITHDRAWN;
    this.deletedAt = now;
  }

}
