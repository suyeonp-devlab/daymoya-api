package com.app.daymoya.domain.tasks.entity;

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
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 일정 제목
  @Column(nullable = false, length = 100)
  private String title;

  // 일정 설명
  private String description;

  // 일정 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TaskStatus status;

  // 시작 시각
  private LocalDateTime startAt;

  // 마감 시각
  @Column(nullable = false)
  private LocalDateTime dueAt;

  // 완료 시각
  private LocalDateTime completedAt;

  // 카테고리 ID
  private Long categoryId;

  // 생성자 ID
  @Column(nullable = false)
  private Long createdBy;

  // 담당자 ID
  @Column(nullable = false)
  private Long assigneeId;

  // 그룹 ID
  private Long groupId;

  // 삭제 시각
  private LocalDateTime deletedAt;

}
