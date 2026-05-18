package com.app.daymoya.domain.tasks.dto.response;

import com.app.daymoya.domain.tasks.entity.Task;
import com.app.daymoya.domain.tasks.entity.TaskPriority;
import com.app.daymoya.domain.tasks.entity.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {

  private Long taskId;                // 일정 PK
  private String title;               // 일정명
  private String description;         // 일정설명
  private TaskStatus status;          // 상태 (코드)
  private String statusLabel;         // 상태 (명)
  private TaskPriority priority;      // 우선순위 (코드)
  private String priorityLabel;       // 우선순위 (명)
  private LocalDateTime startAt;      // 시작일시
  private LocalDateTime endAt;        // 종료일시
  private LocalDateTime completedAt;  // 완료일시
  private Long categoryId;            // 카테고리 ID
  private String categoryName;        // 카테고리명

  public static TaskResponse from(Task task, String categoryName) {

    return TaskResponse.builder()
      .taskId(task.getId())
      .title(task.getTitle())
      .description(task.getDescription())
      .status(task.getStatus())
      .statusLabel(task.getStatus().getLabel())
      .priority(task.getPriority())
      .priorityLabel(task.getPriority().getLabel())
      .startAt(task.getStartAt())
      .endAt(task.getEndAt())
      .completedAt(task.getCompletedAt())
      .categoryId(task.getCategoryId())
      .categoryName(categoryName)
      .build();
  }

}
