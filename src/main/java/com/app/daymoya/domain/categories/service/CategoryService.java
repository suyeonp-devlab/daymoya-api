package com.app.daymoya.domain.categories.service;

import com.app.daymoya.domain.categories.dto.request.CategoryCreateRequest;
import com.app.daymoya.domain.categories.dto.request.CategoryUpdateRequest;
import com.app.daymoya.domain.categories.dto.response.CategoryResponse;
import com.app.daymoya.domain.categories.entity.Category;
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

    // 카테고리명 중복 여부
    boolean duplicate = categoryRepository.existsPersonalCategoryByName(userId, name, null);
    if (duplicate) throw new BizException(CATEGORY_DUPLICATE_NAME);

    // 카테고리 생성
    int sort = categoryRepository.nextPersonalSortNo(userId);
    Category category = Category.createPersonal(
      name, userId, request.getColor(), sort
    );

    categoryRepository.save(category);
  }

  /** 개인 카테고리 수정 */
  @Transactional
  public void updatePersonalCategory(Long userId, Long categoryId, CategoryUpdateRequest request) {

    String name = request.getName().trim().toLowerCase();
    Category category = findPersonalCategory(userId, categoryId);

    // 카테고리명 중복 여부
    boolean duplicate = categoryRepository.existsPersonalCategoryByName(userId, name, categoryId);
    if (duplicate) throw new BizException(CATEGORY_DUPLICATE_NAME);

    // 카테고리 수정
    category.update(name, request.getColor());
  }

  /** 개인 카테고리 삭제 */
  @Transactional
  public void deletePersonalCategory(Long userId, Long categoryId) {

    // TODO : 해당 카테고리로 등록된 일정이 존재하는지 체크

    Category category = findPersonalCategory(userId, categoryId);
    categoryRepository.delete(category);
  }

  /** 개인 카테고리 단건 조회 */
  private Category findPersonalCategory(Long userId, Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isPersonal(userId)) throw new BizException(CATEGORY_ACCESS_DENIED);
    return category;
  }

}
