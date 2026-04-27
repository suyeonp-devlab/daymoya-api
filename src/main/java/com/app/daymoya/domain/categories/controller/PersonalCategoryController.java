package com.app.daymoya.domain.categories.controller;

import com.app.daymoya.domain.categories.dto.request.CreateCategoryRequest;
import com.app.daymoya.domain.categories.dto.request.UpdateCategoryOrderRequest;
import com.app.daymoya.domain.categories.dto.request.UpdateCategoryRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryItem;
import com.app.daymoya.domain.categories.service.CategoryService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/categories")
public class PersonalCategoryController {

  private final CategoryService categoryService;

  /** 개인 카테고리 조회 */
  @GetMapping
  public ApiResponse<List<CategoryItem>> getPersonalCategories(@CurrentUserId Long userId) {

    List<CategoryItem> categories = categoryService.getPersonalCategories(userId);
    return ApiResponse.success(categories);
  }

  /** 개인 카테고리 등록 */
  @PostMapping
  public ApiResponse<Long> createPersonalCategory(@CurrentUserId Long userId
                                                     ,@Valid @RequestBody CreateCategoryRequest request) {

    Long categoryId = categoryService.createPersonalCategory(userId, request);
    return ApiResponse.success(categoryId);
  }

  /** 개인 카테고리 수정 */
  @PatchMapping("/{categoryId}")
  public ApiResponse<Void> updatePersonalCategory(@CurrentUserId Long userId
                                                     ,@PathVariable Long categoryId
                                                     ,@Valid @RequestBody UpdateCategoryRequest request) {

    categoryService.updatePersonalCategory(userId, categoryId, request);
    return ApiResponse.success(null);
  }

  /** 개인 카테고리 삭제 */
  @DeleteMapping("/{categoryId}")
  public ApiResponse<Void> deletePersonalCategory(@CurrentUserId Long userId, @PathVariable Long categoryId) {

    categoryService.deletePersonalCategory(userId, categoryId);
    return ApiResponse.success(null);
  }

  /** 개인 카테고리 순서 변경 */
  @PatchMapping("/order")
  public ApiResponse<Void> updatePersonalCategoryOrder(@CurrentUserId Long userId
                                                          ,@Valid @RequestBody UpdateCategoryOrderRequest request) {

    categoryService.updatePersonalCategoryOrder(userId, request);
    return ApiResponse.success(null);
  }

}
