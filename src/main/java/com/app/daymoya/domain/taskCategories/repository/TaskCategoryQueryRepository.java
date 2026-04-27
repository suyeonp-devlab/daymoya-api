package com.app.daymoya.domain.taskCategories.repository;

import com.app.daymoya.domain.taskCategories.dto.response.TaskCategoryItem;
import com.app.daymoya.domain.taskCategories.entity.QTaskCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.daymoya.domain.taskCategories.entity.TaskCategoryScopeType.*;

@Repository
@RequiredArgsConstructor
public class TaskCategoryQueryRepository {

  private final JPAQueryFactory queryFactory;

  /** 개인 카테고리 조회 */
  public List<TaskCategoryItem> findPersonalTaskCategories(Long scopeUserId) {

    QTaskCategory tc = QTaskCategory.taskCategory;

    return queryFactory
      .select(Projections.constructor(
        TaskCategoryItem.class,
        tc.id,
        tc.name,
        tc.scopeType,
        tc.color,
        tc.displayOrder
      ))
      .from(tc)
      .where(personalScope(tc, scopeUserId))
      .orderBy(tc.scopeType.desc(), tc.displayOrder.asc())
      .fetch();
  }

  /** 개인 카테고리 이름 중복검사 (등록 시 사용) */
  public boolean existsPersonalCategoryName(Long scopeUserId, String name) {

    QTaskCategory tc = QTaskCategory.taskCategory;

    Integer result = queryFactory
      .selectOne()
      .from(tc)
      .where(
        tc.name.eq(name),
        personalScope(tc, scopeUserId)
      )
      .fetchFirst();

    return result != null;
  }

  /** 개인 카테고리 이름 중복검사 - 자기 자신 제외 (수정 시 사용) */
  public boolean existsPersonalCategoryNameForUpdate(Long scopeUserId, Long id, String name) {

    QTaskCategory tc = QTaskCategory.taskCategory;

    Integer result = queryFactory
      .selectOne()
      .from(tc)
      .where(
        tc.name.eq(name),
        tc.id.ne(id),
        personalScope(tc, scopeUserId)
      )
      .fetchFirst();

    return result != null;
  }

  /** 개인 카테고리 추출 조건 */
  private BooleanExpression personalScope(QTaskCategory tc, Long scopeUserId) {

    return tc.scopeType.eq(SYSTEM_PERSONAL)
      .or(tc.scopeType.eq(PERSONAL).and(tc.scopeUserId.eq(scopeUserId)));
  }

}
