package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {

  /** 개인 카테고리 목록 조회 */
  List<Category> findPersonalCategories(Long userId);

  /** 그룹 카테고리 목록 조회 */
  List<Category> findGroupCategories(Long groupId);

}
