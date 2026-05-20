package com.app.daymoya.domain.categories.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryReorderRequest {

  @NotEmpty(message = "카테고리 목록은 필수입니다.")
  private List<Long> categoryIds;

}
