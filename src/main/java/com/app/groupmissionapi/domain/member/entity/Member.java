package com.app.groupmissionapi.domain.member.entity;

import com.app.groupmissionapi.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

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
  @Column(nullable = false, length = 20)
  private String nickname;

  // 상태 (ACTIVE, INACTIVE, SUSPENDED 등)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MemberStatus status;

  // 로그인 실패 횟수
  @Column(nullable = false)
  private int loginFailCount;

  // 계정 잠금 해제 시간
  private LocalDateTime lockedUntil;

  // 비밀번호 변경 시간
  @Column(nullable = false)
  private LocalDateTime passwordChangedAt;

  // 마지막 로그인 시간
  private LocalDateTime lastLoginAt;

  // 마지막 확인 시간 (알림 등)
  private LocalDateTime lastCheckedAt;

}
