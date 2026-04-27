package com.app.daymoya.domain.taskCategories.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateTaskCategoryOrderRequest {

  @NotEmpty(message = "카테고리 순서 목록이 비어있습니다.")
  private List<Long> categoryIds;

}
