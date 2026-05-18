package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {

  /** 개인 카테고리 목록 조회 */
  List<Category> findPersonalCategories(Long userId);

  /** 개인 카테고리명 중복 여부 */
  boolean existsPersonalCategoryByName(Long userId, String name, Long excludeId);

  /** next 개인 카테고리 sort 번호 */
  int nextPersonalSortNo(Long userId);

}
