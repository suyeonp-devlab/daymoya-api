package com.app.daymoya.domain.tasks.repository;

import com.app.daymoya.domain.tasks.entity.QTask;
import com.app.daymoya.domain.tasks.entity.Task;
import com.app.daymoya.domain.tasks.entity.TaskStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QTask task = QTask.task;

  /** 개인 일정 목록 조회 */
  @Override
  public List<Task> findPersonalTasks(Long userId, LocalDateTime from, LocalDateTime to, TaskStatus status) {

    return queryFactory
      .selectFrom(task)
      .where(
        task.assigneeId.eq(userId),
        task.groupId.isNull(),
        task.deletedAt.isNull(),
        inMonth(from, to),
        statusEq(status)
      )
      .orderBy(task.endAt.asc(), task.priority.desc())
      .fetch();
  }

  /** 기간 조건절 */
  private BooleanExpression inMonth(LocalDateTime from, LocalDateTime to) {

    // from <= 종료일 && 종료일 < to
    // 또는
    // from <= 시작일 && 시작일 < to

    BooleanExpression endAtInMonth = task.endAt.goe(from).and(task.endAt.lt(to));
    BooleanExpression startAtInMonth = task.startAt.isNotNull()
      .and(task.startAt.goe(from))
      .and(task.startAt.lt(to));

    return endAtInMonth.or(startAtInMonth);
  }

  /** 상태 조건절 */
  private BooleanExpression statusEq(TaskStatus status) {
    return status != null ? task.status.eq(status) : null;
  }

}
