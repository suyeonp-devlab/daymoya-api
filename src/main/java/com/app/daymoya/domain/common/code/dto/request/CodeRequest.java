package com.app.daymoya.domain.common.code.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeRequest {

  @NotBlank(message = "그룹코드는 필수입니다.")
  private String grpCodeId;

  private String code;

}
