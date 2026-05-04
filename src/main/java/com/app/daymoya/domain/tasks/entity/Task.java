package com.app.daymoya.domain.tasks.entity;

import com.app.daymoya.global.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Task extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 일정명
  @Column(nullable = false, length = 20)
  private String title;

  // 일정설명
  @Column(length = 100)
  private String description;

  // 상태(공통코드 TASK-001)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TaskStatus status;

  // 우선순위(공통코드 TASK-002)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TaskPriority priority;

  // 시작일시
  private LocalDateTime startAt;

  // 종료일시
  @Column(nullable = false)
  private LocalDateTime endAt;

  // 완료일시
  private LocalDateTime completedAt;

  // 카테고리 ID
  private Long categoryId;

  // 담당자 ID
  @Column(nullable = false)
  private Long assigneeId;

  // 그룹 ID
  private Long groupId;

  // 삭제일시
  private LocalDateTime deletedAt;

}
