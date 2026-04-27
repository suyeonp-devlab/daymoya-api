package com.app.daymoya.domain.categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {

  @NotBlank(message = "카테고리명은 필수입니다.")
  @Size(min = 2, max = 10, message = "카테고리명은 2자 이상 10자 이하로 입력해주세요.")
  private String name;

  private String color;

}
