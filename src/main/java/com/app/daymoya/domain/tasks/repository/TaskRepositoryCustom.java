package com.app.daymoya.domain.tasks.repository;

import com.app.daymoya.domain.tasks.entity.Task;
import com.app.daymoya.domain.tasks.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryCustom {

  /** 개인 일정 목록 조회 */
  List<Task> findPersonalTasks(Long userId, LocalDateTime from, LocalDateTime to, TaskStatus status);

}
