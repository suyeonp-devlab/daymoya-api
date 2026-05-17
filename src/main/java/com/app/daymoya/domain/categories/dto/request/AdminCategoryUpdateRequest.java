package com.app.daymoya.domain.categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCategoryUpdateRequest {

  @NotBlank(message = "카테고리명은 필수입니다.")
  @Size(max = 10)
  private String name;

  @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "색상은 #RRGGBB 형식이어야 합니다.")
  private String color;

}
