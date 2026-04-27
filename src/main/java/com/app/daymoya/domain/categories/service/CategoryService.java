package com.app.daymoya.domain.categories.service;

import com.app.daymoya.domain.categories.dto.request.CreateCategoryRequest;
import com.app.daymoya.domain.categories.dto.request.UpdateCategoryOrderRequest;
import com.app.daymoya.domain.categories.dto.request.UpdateCategoryRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryItem;
import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.repository.CategoryQueryRepository;
import com.app.daymoya.domain.categories.repository.CategoryRepository;
import com.app.daymoya.domain.tasks.repository.TaskRepository;
import com.app.daymoya.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.app.daymoya.domain.categories.entity.CategoryScopeType.*;
import static com.app.daymoya.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final TaskRepository taskRepository;

  /** 개인 카테고리 조회 */
  public List<CategoryItem> getPersonalCategories(Long userId) {
    return categoryQueryRepository.findPersonalCategories(userId);
  }

  /** 개인 카테고리 등록 */
  @Transactional
  public Long createPersonalCategory(Long userId, CreateCategoryRequest request) {

    String categoryName = request.getName().trim().toLowerCase();

    // 카테고리 이름 중복 검사
    boolean existsName = categoryQueryRepository.existsPersonalCategoryName(userId, categoryName);

    if (existsName) {
      throw new CustomException(CATEGORY_NAME_DUPLICATED);
    }

    // 마지막 순서 + 1 조회
    Integer nextDisplayOrder = categoryRepository
      .findTopByScopeTypeAndScopeUserIdOrderByDisplayOrderDesc(PERSONAL, userId)
      .map(category -> category.getDisplayOrder() + 1)
      .orElse(1);

    // 엔티티 생성 및 저장
    Category category = Category.createPersonal(
      categoryName,
      userId,
      request.getColor(),
      nextDisplayOrder
    );

    return categoryRepository.save(category).getId();
  }

  /** 개인 카테고리 수정 */
  @Transactional
  public void updatePersonalCategory(Long userId, Long categoryId, UpdateCategoryRequest request) {

    String categoryName = request.getName().trim().toLowerCase();

    // 카테고리 조회
    Category category = categoryRepository
      .findByIdAndScopeTypeAndScopeUserId(categoryId, PERSONAL, userId)
      .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

    // 카테고리 이름 중복 검사 (자기 자신 제외)
    boolean existsName = categoryQueryRepository.existsPersonalCategoryNameForUpdate(
      userId, categoryId, categoryName
    );

    if (existsName) {
      throw new CustomException(CATEGORY_NAME_DUPLICATED);
    }

    // 카테고리 수정
    category.update(categoryName, request.getColor());
  }

  /** 개인 카테고리 삭제 */
  @Transactional
  public void deletePersonalCategory(Long userId, Long categoryId) {

    // 카테고리 조회
    Category category = categoryRepository
      .findByIdAndScopeTypeAndScopeUserId(categoryId, PERSONAL, userId)
      .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

    // 카테고리 내 일정 존재 여부
    boolean existsTask = taskRepository.existsByCategoryIdAndDeletedAtIsNull(categoryId);
    if (existsTask) throw new CustomException(CATEGORY_IN_USE);

    // 카테고리 삭제
    categoryRepository.delete(category);
  }

  /** 개인 카테고리 순서 변경 */
  @Transactional
  public void updatePersonalCategoryOrder(Long userId, UpdateCategoryOrderRequest request) {

    List<Long> categoryIds = request.getCategoryIds();

    // 요청값 중복 체크
    Set<Long> distinctIds = new HashSet<>(categoryIds);
    if (distinctIds.size() != categoryIds.size()) {
      throw new CustomException(CATEGORY_ID_DUPLICATED);
    }

    // 카테고리 조회
    List<Category> categories = categoryRepository.findAllByIdInAndScopeTypeAndScopeUserId(
      categoryIds, PERSONAL, userId
    );

    if (categories.size() != categoryIds.size()) {
      throw new CustomException(CATEGORY_NOT_FOUND);
    }

    // 카테고리 정렬 (요청 list 순서대로 displayOrder 부여)
    Map<Long, Category> categoryMap = categories.stream()
      .collect(Collectors.toMap(Category::getId, Function.identity()));

    for (int i = 0; i < categoryIds.size(); i++) {
      Category category = categoryMap.get(categoryIds.get(i));
      category.updateDisplayOrder(i + 1);
    }
  }

}
