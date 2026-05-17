package com.app.daymoya.domain.categories.dto.response;

import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.entity.CategoryScope;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

  private Long categoryId;      // 카테고리 PK
  private String name;          // 카테고리명
  private CategoryScope scope;  // 범위
  private String color;         // 색상
  private Integer sortNo;       // 정렬순서

  public static CategoryResponse from(Category category) {

    return CategoryResponse.builder()
      .categoryId(category.getId())
      .name(category.getName())
      .scope(category.getScope())
      .color(category.getColor())
      .sortNo(category.getSortNo())
      .build();
  }

}
