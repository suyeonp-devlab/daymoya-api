package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.dto.response.CategoryItem;
import com.app.daymoya.domain.categories.entity.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.daymoya.domain.categories.entity.CategoryScopeType.*;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

  private final JPAQueryFactory queryFactory;

  /** 개인 카테고리 조회 */
  public List<CategoryItem> findPersonalCategories(Long scopeUserId) {

    QCategory category = QCategory.category;

    return queryFactory
      .select(Projections.constructor(
        CategoryItem.class,
        category.id,
        category.name,
        category.scopeType,
        category.color,
        category.displayOrder
      ))
      .from(category)
      .where(personalScope(category, scopeUserId))
      .orderBy(category.scopeType.desc(), category.displayOrder.asc())
      .fetch();
  }

  /** 개인 카테고리 이름 중복검사 (등록 시 사용) */
  public boolean existsPersonalCategoryName(Long scopeUserId, String name) {

    QCategory category = QCategory.category;

    Integer result = queryFactory
      .selectOne()
      .from(category)
      .where(
        category.name.eq(name),
        personalScope(category, scopeUserId)
      )
      .fetchFirst();

    return result != null;
  }

  /** 개인 카테고리 이름 중복검사 - 자기 자신 제외 (수정 시 사용) */
  public boolean existsPersonalCategoryNameForUpdate(Long scopeUserId, Long id, String name) {

    QCategory category = QCategory.category;

    Integer result = queryFactory
      .selectOne()
      .from(category)
      .where(
        category.name.eq(name),
        category.id.ne(id),
        personalScope(category, scopeUserId)
      )
      .fetchFirst();

    return result != null;
  }

  /** 개인 카테고리 추출 조건 */
  private BooleanExpression personalScope(QCategory category, Long scopeUserId) {

    return category.scopeType.eq(SYSTEM_PERSONAL)
      .or(category.scopeType.eq(PERSONAL).and(category.scopeUserId.eq(scopeUserId)));
  }

}
