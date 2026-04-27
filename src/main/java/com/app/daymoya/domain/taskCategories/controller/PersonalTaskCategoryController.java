package com.app.daymoya.domain.taskCategories.controller;

import com.app.daymoya.domain.taskCategories.dto.request.CreateTaskCategoryRequest;
import com.app.daymoya.domain.taskCategories.dto.request.UpdateTaskCategoryOrderRequest;
import com.app.daymoya.domain.taskCategories.dto.request.UpdateTaskCategoryRequest;
import com.app.daymoya.domain.taskCategories.dto.response.TaskCategoryItem;
import com.app.daymoya.domain.taskCategories.service.TaskCategoryService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/task-categories")
public class PersonalTaskCategoryController {

  private final TaskCategoryService taskCategoryService;

  /** 개인 카테고리 조회 */
  @GetMapping
  public ApiResponse<List<TaskCategoryItem>> getPersonalTaskCategories(@CurrentUserId Long userId) {

    List<TaskCategoryItem> categories = taskCategoryService.getPersonalTaskCategories(userId);
    return ApiResponse.success(categories);
  }

  /** 개인 카테고리 등록 */
  @PostMapping
  public ApiResponse<Long> createPersonalTaskCategory(@CurrentUserId Long userId
                                                     ,@Valid @RequestBody CreateTaskCategoryRequest request) {

    Long categoryId = taskCategoryService.createPersonalTaskCategory(userId, request);
    return ApiResponse.success(categoryId);
  }

  /** 개인 카테고리 수정 */
  @PatchMapping("/{categoryId}")
  public ApiResponse<Void> updatePersonalTaskCategory(@CurrentUserId Long userId
                                                     ,@PathVariable Long categoryId
                                                     ,@Valid @RequestBody UpdateTaskCategoryRequest request) {

    taskCategoryService.updatePersonalTaskCategory(userId, categoryId, request);
    return ApiResponse.success(null);
  }

  /** 개인 카테고리 삭제 */
  @DeleteMapping("/{categoryId}")
  public ApiResponse<Void> deletePersonalTaskCategory(@CurrentUserId Long userId, @PathVariable Long categoryId) {

    taskCategoryService.deletePersonalTaskCategory(userId, categoryId);
    return ApiResponse.success(null);
  }

  /** 개인 카테고리 순서 변경 */
  @PatchMapping("/order")
  public ApiResponse<Void> updatePersonalTaskCategoryOrder(@CurrentUserId Long userId
                                                          ,@Valid @RequestBody UpdateTaskCategoryOrderRequest request) {

    taskCategoryService.updatePersonalTaskCategoryOrder(userId, request);
    return ApiResponse.success(null);
  }

}
