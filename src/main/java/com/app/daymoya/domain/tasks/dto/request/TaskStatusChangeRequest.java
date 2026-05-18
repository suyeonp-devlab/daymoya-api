package com.app.daymoya.domain.tasks.dto.request;

import com.app.daymoya.domain.tasks.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskStatusChangeRequest {

  @NotNull(message = "상태는 필수입니다.")
  private TaskStatus status;

}
