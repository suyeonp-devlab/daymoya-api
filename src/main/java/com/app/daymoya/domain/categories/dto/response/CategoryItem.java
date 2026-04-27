package com.app.daymoya.domain.categories.dto.response;

import com.app.daymoya.domain.categories.entity.CategoryScopeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryItem {

  private Long categoryId;
  private String name;
  private CategoryScopeType scopeType;
  private String color;
  private Integer displayOrder;

}
