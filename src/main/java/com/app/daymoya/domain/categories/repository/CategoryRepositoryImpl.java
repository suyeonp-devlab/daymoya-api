package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.entity.CategoryScope;
import com.app.daymoya.domain.categories.entity.QCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QCategory category = QCategory.category;

  /** 개인 카테고리 목록 조회 */
  @Override
  public List<Category> findPersonalCategories(Long userId) {

    return queryFactory
      .selectFrom(category)
      .where(isPersonalScope(userId))
      .orderBy(category.scope.asc(), category.sortNo.asc())
      .fetch();
  }

  /** 그룹 카테고리 목록 조회 */
  @Override
  public List<Category> findGroupCategories(Long groupId) {

    return queryFactory
      .selectFrom(category)
      .where(isGroupScope(groupId))
      .orderBy(category.scope.asc(), category.sortNo.asc())
      .fetch();
  }

  /** 개인 카테고리 조회 조건 */
  private BooleanExpression isPersonalScope(Long userId) {
    return category.scope.eq(CategoryScope.SYS_PERSONAL)
      .or(category.scope.eq(CategoryScope.PERSONAL).and(category.scopeUserId.eq(userId)));
  }

  /** 그룹 카테고리 조회 조건 */
  private BooleanExpression isGroupScope(Long groupId) {
    return category.scope.eq(CategoryScope.SYS_GROUP)
      .or(category.scope.eq(CategoryScope.GROUP).and(category.scopeGroupId.eq(groupId)));
  }

}
