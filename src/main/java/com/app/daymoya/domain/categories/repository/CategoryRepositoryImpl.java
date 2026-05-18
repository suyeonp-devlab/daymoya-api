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

  /** 개인 카테고리명 중복 여부 */
  @Override
  public boolean existsPersonalCategoryByName(Long userId, String name, Long excludeId) {

    return queryFactory
      .selectOne()
      .from(category)
      .where(
        isPersonalScope(userId),
        category.name.eq(name),
        excludeId(excludeId)
      )
      .fetchFirst() != null;
  }

  /** next 개인 카테고리 sort 번호 */
  @Override
  public int nextPersonalSortNo(Long userId) {

    Integer max = queryFactory
      .select(category.sortNo.max())
      .from(category)
      .where(
        category.scope.eq(CategoryScope.PERSONAL),
        category.scopeUserId.eq(userId)
      )
      .fetchOne();

    return max != null ? max + 1 : 1;
  }

  /** 개인 카테고리 조회 조건 */
  private BooleanExpression isPersonalScope(Long userId) {
    return category.scope.eq(CategoryScope.SYS_PERSONAL)
      .or(category.scope.eq(CategoryScope.PERSONAL).and(category.scopeUserId.eq(userId)));
  }

  /** 특정 카테고리 제외 여부 */
  private BooleanExpression excludeId(Long excludeId) {
    return excludeId != null ? category.id.ne(excludeId) : null;
  }

}
