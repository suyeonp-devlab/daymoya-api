package com.app.daymoya.domain.taskCategories.repository;

import com.app.daymoya.domain.taskCategories.entity.TaskCategory;
import com.app.daymoya.domain.taskCategories.entity.TaskCategoryScopeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

  Optional<TaskCategory> findByIdAndScopeTypeAndScopeUserId(
    Long id,
    TaskCategoryScopeType scopeType,
    Long scopeUserId
  );

  Optional<TaskCategory> findTopByScopeTypeAndScopeUserIdOrderByDisplayOrderDesc(
    TaskCategoryScopeType scopeType,
    Long scopeUserId
  );

  List<TaskCategory> findAllByIdInAndScopeTypeAndScopeUserId(
    List<Long> ids,
    TaskCategoryScopeType scopeType,
    Long scopeUserId
  );

}
