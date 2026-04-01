package com.app.daymoya.domain.schedule.member.entity;

import com.app.daymoya.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "schedule_space_members", uniqueConstraints = {
  @UniqueConstraint(name = "uk_ssm_space_member", columnNames = {"schedule_space_id", "member_id"})
})
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleSpaceMember extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 스케줄 공간 ID
  @Column(nullable = false)
  private Long scheduleSpaceId;

  // 회원 ID
  @Column(nullable = false)
  private Long memberId;

  // 역할 (OWNER 소유자, MEMBER 그룹원)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SpaceMemberRole role;

  // 상태 (ACTIVE 활성, LEFT 자발적 탈퇴, REMOVED 강제 제외)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SpaceMemberStatus status;

  /** 참여 시각 */
  @Column(nullable = false)
  private LocalDateTime joinedAt;

  /** 스케줄 공간 회원 생성 */
  public static ScheduleSpaceMember create(Long scheduleSpaceId
                                          ,Long memberId
                                          ,SpaceMemberRole role
                                          ,LocalDateTime now) {

    return ScheduleSpaceMember.builder()
      .scheduleSpaceId(scheduleSpaceId)
      .memberId(memberId)
      .role(role)
      .status(SpaceMemberStatus.ACTIVE)
      .joinedAt(now)
      .build();
  }

}