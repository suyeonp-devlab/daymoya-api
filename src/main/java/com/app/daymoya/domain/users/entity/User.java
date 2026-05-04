package com.app.daymoya.domain.users.entity;

import com.app.daymoya.global.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseAuditEntity {

  private static final int PASSWORD_FAIL_CNT = 5;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 이메일(로그인 ID)
  @Column(unique = true)
  private String email;

  // 비밀번호
  private String password;

  // 닉네임
  @Column(nullable = false, length = 50)
  private String nickname;

  // 프로필 이미지 경로
  @Column(length = 500)
  private String profileImagePath;

  // 권한(공통코드 USER-001)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserRole role;

  // 상태(공통코드 USER-002)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserStatus status;

  // 연속 로그인 실패 횟수
  @Column(nullable = false)
  private Integer failedLoginCount;

  // 비밀번호 변경 시각
  private LocalDateTime passwordChangedAt;

  // 로그인 시각
  private LocalDateTime lastLoginAt;

  // 탈퇴일시
  private LocalDateTime deletedAt;

}
