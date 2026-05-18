package com.app.daymoya.domain.tasks.controller;

import com.app.daymoya.domain.tasks.dto.request.TaskCreateRequest;
import com.app.daymoya.domain.tasks.dto.request.TaskStatusChangeRequest;
import com.app.daymoya.domain.tasks.dto.request.TaskUpdateRequest;
import com.app.daymoya.domain.tasks.dto.response.TaskResponse;
import com.app.daymoya.domain.tasks.entity.TaskStatus;
import com.app.daymoya.domain.tasks.service.TaskService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  /** 개인 일정 목록 조회 */
  @GetMapping
  public ApiResponse<List<TaskResponse>> getPersonalTasks(
    @CurrentUserId Long userId,
    @RequestParam String yearMonth,
    @RequestParam(required = false) TaskStatus status
  ) {
    List<TaskResponse> tasks = taskService.getPersonalTasks(userId, yearMonth, status);
    return ApiResponse.success(tasks);
  }

  /** 개인 일정 생성 */
  @PostMapping
  public ApiResponse<Void> createPersonalTask(@CurrentUserId Long userId, @Valid @RequestBody TaskCreateRequest request) {
    taskService.createPersonalTask(userId, request);
    return ApiResponse.success();
  }

  /** 개인 일정 수정 */
  @PatchMapping("/{taskId}")
  public ApiResponse<Void> updatePersonalTask(
    @CurrentUserId Long userId,
    @PathVariable Long taskId,
    @Valid @RequestBody TaskUpdateRequest request
  ) {
    taskService.updatePersonalTask(userId, taskId, request);
    return ApiResponse.success();
  }

  /** 개인 일정 삭제 */
  @DeleteMapping("/{taskId}")
  public ApiResponse<Void> deletePersonalTask(@CurrentUserId Long userId, @PathVariable Long taskId) {
    taskService.deletePersonalTask(userId, taskId);
    return ApiResponse.success();
  }

  /** 개인 일정 상태 변경 */
  @PatchMapping("/{taskId}/status")
  public ApiResponse<Void> changePersonalTaskStatus(
    @CurrentUserId Long userId,
    @PathVariable Long taskId,
    @Valid @RequestBody TaskStatusChangeRequest request
  ) {
    taskService.changePersonalTaskStatus(userId, taskId, request);
    return ApiResponse.success();
  }

}
