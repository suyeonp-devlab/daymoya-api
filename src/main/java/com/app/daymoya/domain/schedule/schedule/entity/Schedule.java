package com.app.daymoya.domain.schedule.schedule.entity;

import com.app.daymoya.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Schedule extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 스케줄 공간 ID
  @Column(nullable = false)
  private Long scheduleSpaceId;

  // 스케줄 생성 회원 ID
  @Column(nullable = false)
  private Long createdMemberId;

  // 스케줄 담당 회원 ID
  @Column(nullable = false)
  private Long assigneeMemberId;

  // 스케줄 제목
  @Column(nullable = false, length = 100)
  private String title;

  // 스케줄 설명
  private String description;

  // 시작 일시
  @Column(nullable = false)
  private LocalDateTime startAt;

  // 종료 일시
  @Column(nullable = false)
  private LocalDateTime endAt;

  // 스케줄 상태 (IN_PROGRESS 진행중, COMPLETED 완료, COMPLETED_LATE 지연 완료, CANCELED 삭제)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ScheduleStatus status;

  // 완료 시각
  private LocalDateTime completedAt;

  // 삭제 시각
  private LocalDateTime canceledAt;

}