package com.app.daymoya.domain.taskCategories.dto.response;

import com.app.daymoya.domain.taskCategories.entity.TaskCategoryScopeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCategoryItem {

  private Long categoryId;
  private String name;
  private TaskCategoryScopeType scopeType;
  private String color;
  private Integer displayOrder;

}
