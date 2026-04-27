package com.app.daymoya.domain.taskCategories.service;

import com.app.daymoya.domain.taskCategories.dto.request.CreateTaskCategoryRequest;
import com.app.daymoya.domain.taskCategories.dto.request.UpdateTaskCategoryOrderRequest;
import com.app.daymoya.domain.taskCategories.dto.request.UpdateTaskCategoryRequest;
import com.app.daymoya.domain.taskCategories.dto.response.TaskCategoryItem;
import com.app.daymoya.domain.taskCategories.entity.TaskCategory;
import com.app.daymoya.domain.taskCategories.repository.TaskCategoryQueryRepository;
import com.app.daymoya.domain.taskCategories.repository.TaskCategoryRepository;
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

import static com.app.daymoya.domain.taskCategories.entity.TaskCategoryScopeType.*;
import static com.app.daymoya.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskCategoryService {

  private final TaskCategoryRepository taskCategoryRepository;
  private final TaskCategoryQueryRepository taskCategoryQueryRepository;
  private final TaskRepository taskRepository;

  /** 개인 카테고리 조회 */
  public List<TaskCategoryItem> getPersonalTaskCategories(Long userId) {
    return taskCategoryQueryRepository.findPersonalTaskCategories(userId);
  }

  /** 개인 카테고리 등록 */
  @Transactional
  public Long createPersonalTaskCategory(Long userId, CreateTaskCategoryRequest request) {

    String categoryName = request.getName().trim().toLowerCase();

    // 카테고리 이름 중복 검사
    boolean existsName = taskCategoryQueryRepository.existsPersonalCategoryName(userId, categoryName);

    if (existsName) {
      throw new CustomException(CATEGORY_NAME_DUPLICATED);
    }

    // 마지막 순서 + 1 조회
    Integer nextDisplayOrder = taskCategoryRepository
      .findTopByScopeTypeAndScopeUserIdOrderByDisplayOrderDesc(PERSONAL, userId)
      .map(taskCategory -> taskCategory.getDisplayOrder() + 1)
      .orElse(1);

    // 엔티티 생성 및 저장
    TaskCategory taskCategory = TaskCategory.createPersonal(
      categoryName,
      userId,
      request.getColor(),
      nextDisplayOrder
    );

    return taskCategoryRepository.save(taskCategory).getId();
  }

  /** 개인 카테고리 수정 */
  @Transactional
  public void updatePersonalTaskCategory(Long userId, Long categoryId, UpdateTaskCategoryRequest request) {

    String categoryName = request.getName().trim().toLowerCase();

    // 카테고리 조회
    TaskCategory taskCategory = taskCategoryRepository
      .findByIdAndScopeTypeAndScopeUserId(categoryId, PERSONAL, userId)
      .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

    // 카테고리 이름 중복 검사 (자기 자신 제외)
    boolean existsName = taskCategoryQueryRepository.existsPersonalCategoryNameForUpdate(
      userId, categoryId, categoryName
    );

    if (existsName) {
      throw new CustomException(CATEGORY_NAME_DUPLICATED);
    }

    // 카테고리 수정
    taskCategory.update(categoryName, request.getColor());
  }

  /** 개인 카테고리 삭제 */
  @Transactional
  public void deletePersonalTaskCategory(Long userId, Long categoryId) {

    // 카테고리 조회
    TaskCategory taskCategory = taskCategoryRepository
      .findByIdAndScopeTypeAndScopeUserId(categoryId, PERSONAL, userId)
      .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));

    // 카테고리 내 일정 존재 여부
    boolean existsTask = taskRepository.existsByCategoryIdAndDeletedAtIsNull(categoryId);
    if (existsTask) throw new CustomException(CATEGORY_IN_USE);

    // 카테고리 삭제
    taskCategoryRepository.delete(taskCategory);
  }

  /** 개인 카테고리 순서 변경 */
  @Transactional
  public void updatePersonalTaskCategoryOrder(Long userId, UpdateTaskCategoryOrderRequest request) {

    List<Long> categoryIds = request.getCategoryIds();

    // 요청값 중복 체크
    Set<Long> distinctIds = new HashSet<>(categoryIds);
    if (distinctIds.size() != categoryIds.size()) {
      throw new CustomException(CATEGORY_ID_DUPLICATED);
    }

    // 카테고리 조회
    List<TaskCategory> categories = taskCategoryRepository.findAllByIdInAndScopeTypeAndScopeUserId(
      categoryIds, PERSONAL, userId
    );

    if (categories.size() != categoryIds.size()) {
      throw new CustomException(CATEGORY_NOT_FOUND);
    }

    // 카테고리 정렬 (요청 list 순서대로 displayOrder 부여)
    Map<Long, TaskCategory> categoryMap = categories.stream()
      .collect(Collectors.toMap(TaskCategory::getId, Function.identity()));

    for (int i = 0; i < categoryIds.size(); i++) {
      TaskCategory category = categoryMap.get(categoryIds.get(i));
      category.updateDisplayOrder(i + 1);
    }
  }

}
