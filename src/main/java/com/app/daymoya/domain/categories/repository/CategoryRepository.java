package com.app.daymoya.domain.categories.repository;

import com.app.daymoya.domain.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
