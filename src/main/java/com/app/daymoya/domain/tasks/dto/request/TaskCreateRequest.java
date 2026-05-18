package com.app.daymoya.domain.tasks.dto.request;

import com.app.daymoya.domain.tasks.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TaskCreateRequest {

  @NotBlank(message = "일정명은 필수입니다.")
  @Size(max = 20, message = "일정명은 20자 이하이어야 합니다.")
  private String title;

  @Size(max = 100, message = "일정 설명은 100자 이하이어야 합니다.")
  private String description;

  @NotNull(message = "우선순위는 필수입니다.")
  private TaskPriority priority;

  private LocalDateTime startAt;

  @NotNull(message = "종료일시는 필수입니다.")
  private LocalDateTime endAt;

  @NotNull(message = "카테고리는 필수입니다.")
  private Long categoryId;

}
