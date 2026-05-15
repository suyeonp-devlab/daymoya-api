package com.app.daymoya.domain.codes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeCreateRequest {

  @NotBlank(message = "코드는 필수입니다.")
  @Size(max = 20, message = "코드는 20자 이하로 입력해주세요.")
  private String code;

  @NotBlank(message = "코드명은 필수입니다.")
  @Size(max = 50, message = "코드명은 50자 이하로 입력해주세요.")
  private String codeName;

  private String description;
  private String etc1;
  private String etc2;
  private String etc3;

  @NotNull(message = "사용여부는 필수입니다.")
  private Boolean enabled;

  @NotNull(message = "정렬순서는 필수입니다.")
  private Integer sortNo;

}
