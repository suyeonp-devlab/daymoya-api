package com.app.daymoya.domain.categories.service;

import com.app.daymoya.domain.categories.dto.request.AdminCategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.AdminCategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryResponse;
import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.entity.CategoryScope;
import com.app.daymoya.domain.categories.repository.CategoryRepository;
import com.app.daymoya.global.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app.daymoya.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /** 개인 카테고리 목록 조회 */
  public List<CategoryResponse> getPersonalCategories(Long userId) {

    return categoryRepository.findPersonalCategories(userId).stream()
      .map(CategoryResponse::from)
      .toList();
  }

  /** 개인 카테고리 생성 */
  @Transactional
  public void createPersonalCategory(Long userId, CategoryCreateRequest request) {

    String name = request.getName().trim().toLowerCase();

    // TODO : 카테고리명 중복체크
    // TODO : sort는 개인 카테고리 마지막 sort번호 + 1
    int sort = 0;

    // 개인 카테고리 등록
    Category category = Category.createPersonal(
      name,
      userId,
      request.getColor(),
      sort
    );

    categoryRepository.save(category);
  }

  /** 개인 카테고리 수정 */
  @Transactional
  public void updatePersonalCategory(Long userId, Long categoryId, CategoryUpdateRequest request) {

    String name = request.getName().trim().toLowerCase();

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isPersonal(userId)) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    // TODO : 카테고리명 중복체크

    // 카테고리 수정
    category.update(name, request.getColor());
  }

  /** 개인 카테고리 삭제 */
  @Transactional
  public void deletePersonalCategory(Long userId, Long categoryId) {

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isPersonal(userId)) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    categoryRepository.delete(category);
  }

  /** 그룹 카테고리 목록 조회 */
  public List<CategoryResponse> getGroupCategories(Long userId, Long groupId) {

    // TODO : userId가 해당 그룹에 속하는지 확인

    return categoryRepository.findGroupCategories(groupId).stream()
      .map(CategoryResponse::from)
      .toList();
  }

  /** 그룹 카테고리 생성 */
  @Transactional
  public void createGroupCategory(Long userId, Long groupId, CategoryCreateRequest request) {

    String name = request.getName().trim().toLowerCase();

    // TODO : userId가 해당 그룹에 속하는지 확인
    // TODO : 카테고리명 중복체크
    // TODO : sort는 개인 카테고리 마지막 sort번호 + 1
    int sort = 0;

    Category category = Category.createGroup(
      name,
      groupId,
      request.getColor(),
      sort
    );

    categoryRepository.save(category);
  }

  /** 그룹 카테고리 수정 */
  @Transactional
  public void updateGroupCategory(Long userId, Long groupId, Long categoryId, CategoryUpdateRequest request) {

    // TODO : userId가 해당 그룹에 속하는지 확인

    String name = request.getName().trim().toLowerCase();

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isGroup(groupId)) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    // TODO : 카테고리명 중복체크

    // 카테고리 수정
    category.update(name, request.getColor());
  }

  /** 그룹 카테고리 삭제 */
  @Transactional
  public void deleteGroupCategory(Long userId, Long groupId, Long categoryId) {

    // TODO : userId가 해당 그룹에 속하는지 확인

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isGroup(groupId)) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    categoryRepository.delete(category);
  }

  /** 시스템 카테고리 생성 */
  @Transactional
  public void createSystemCategory(AdminCategoryCreateRequest request) {

    String name = request.getName().trim().toLowerCase();
    // TODO : 카테고리명 중복체크
    // TODO : sort는 개인 카테고리 마지막 sort번호 + 1
    int sort = 0;

    Category category = Category.createSystem(
      name,
      request.getCategoryScope(),
      request.getColor(),
      sort
    );

    categoryRepository.save(category);
  }

  /** 시스템 카테고리 수정 */
  @Transactional
  public void updateSystemCategory(Long categoryId, AdminCategoryUpdateRequest request) {

    String name = request.getName().trim().toLowerCase();

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isSystem()) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    // TODO : 카테고리명 중복체크

    // 카테고리 수정
    category.update(name, request.getColor());
  }

  /** 시스템 카테고리 삭제 */
  @Transactional
  public void deleteSystemCategory(Long categoryId) {

    // 카테고리 조회
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isSystem()) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }

    categoryRepository.delete(category);
  }

}
