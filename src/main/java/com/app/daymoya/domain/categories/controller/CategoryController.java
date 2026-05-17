package com.app.daymoya.domain.categories.controller;

import com.app.daymoya.domain.categories.dto.request.AdminCategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.AdminCategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryResponse;
import com.app.daymoya.domain.categories.entity.CategoryScope;
import com.app.daymoya.domain.categories.service.CategoryService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  /** 개인 카테고리 목록 조회 */
  @GetMapping
  public ApiResponse<List<CategoryResponse>> getPersonalCategories(@CurrentUserId Long userId) {
    List<CategoryResponse> categories = categoryService.getPersonalCategories(userId);
    return ApiResponse.success(categories);
  }

  /** 개인 카테고리 생성 */
  @PostMapping
  public ApiResponse<Void> createPersonalCategory(@CurrentUserId Long userId, @Valid @RequestBody CategoryCreateRequest request) {
    categoryService.createPersonalCategory(userId, request);
    return ApiResponse.success();
  }

  /** 개인 카테고리 수정 */
  @PatchMapping("/{categoryId}")
  public ApiResponse<Void> updatePersonalCategory(
    @CurrentUserId Long userId,
    @PathVariable Long categoryId,
    @Valid @RequestBody CategoryUpdateRequest request
  ) {
    categoryService.updatePersonalCategory(categoryId, userId, request);
    return ApiResponse.success();
  }

  /** 개인 카테고리 삭제 */
  @DeleteMapping("/{categoryId}")
  public ApiResponse<Void> deletePersonalCategory(@CurrentUserId Long userId, @PathVariable Long categoryId) {
    categoryService.deletePersonalCategory(categoryId, userId);
    return ApiResponse.success();
  }

  /** 시스템 카테고리 생성 */
  @PostMapping("/admin")
  public ApiResponse<Void> createSystemCategory(@Valid @RequestBody AdminCategoryCreateRequest request) {
    categoryService.createSystemCategory(request);
    return ApiResponse.success();
  }

  /** 시스템 카테고리 수정 */
  @PatchMapping("/admin/{categoryId}")
  public ApiResponse<Void> updateSystemCategory(@PathVariable Long categoryId, @Valid @RequestBody AdminCategoryUpdateRequest request) {
    categoryService.updateSystemCategory(categoryId, request);
    return ApiResponse.success();
  }

  /** 시스템 카테고리 삭제 */
  @DeleteMapping("/admin/{categoryId}")
  public ApiResponse<Void> deleteSystemCategory(@PathVariable Long categoryId) {
    categoryService.deleteSystemCategory(categoryId);
    return ApiResponse.success();
  }

}
