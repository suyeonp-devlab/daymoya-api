package com.app.daymoya.domain.categories.controller;

import com.app.daymoya.domain.categories.dto.request.CategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryResponse;
import com.app.daymoya.domain.categories.service.CategoryService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/categories")
public class GroupCategoryController {

  private final CategoryService categoryService;

  /** 그룹 카테고리 목록 조회 */
  @GetMapping
  public ApiResponse<List<CategoryResponse>> getGroupCategories(@CurrentUserId Long userId, @PathVariable Long groupId) {
    List<CategoryResponse> categories = categoryService.getGroupCategories(userId, groupId);
    return ApiResponse.success(categories);
  }

  /** 그룹 카테고리 생성 */
  @PostMapping
  public ApiResponse<Void> createGroupCategory(
    @CurrentUserId Long userId,
    @PathVariable Long groupId,
    @Valid @RequestBody CategoryCreateRequest request
  ) {
    categoryService.createGroupCategory(userId, groupId, request);
    return ApiResponse.success();
  }

  /** 그룹 카테고리 수정 */
  @PatchMapping("/{categoryId}")
  public ApiResponse<Void> updateGroupCategory(
    @CurrentUserId Long userId,
    @PathVariable Long groupId,
    @PathVariable Long categoryId,
    @Valid @RequestBody CategoryUpdateRequest request
  ) {
    categoryService.updateGroupCategory(userId, groupId, categoryId, request);
    return ApiResponse.success();
  }

  /** 그룹 카테고리 삭제 */
  @DeleteMapping("/{categoryId}")
  public ApiResponse<Void> deleteGroupCategory(
    @CurrentUserId Long userId,
    @PathVariable Long groupId,
    @PathVariable Long categoryId
  ) {
    categoryService.deleteGroupCategory(userId, groupId, categoryId);
    return ApiResponse.success();
  }

}
