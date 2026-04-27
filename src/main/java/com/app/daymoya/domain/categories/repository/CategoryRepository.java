package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.entity.CategoryScopeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByIdAndScopeTypeAndScopeUserId(
    Long id,
    CategoryScopeType scopeType,
    Long scopeUserId
  );

  Optional<Category> findTopByScopeTypeAndScopeUserIdOrderByDisplayOrderDesc(
    CategoryScopeType scopeType,
    Long scopeUserId
  );

  List<Category> findAllByIdInAndScopeTypeAndScopeUserId(
    List<Long> ids,
    CategoryScopeType scopeType,
    Long scopeUserId
  );

}
