package com.app.daymoya.domain.codes.dto.request;

import com.app.daymoya.domain.codes.entity.CodeGroupDomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeGroupCreateRequest {

  @NotBlank(message = "그룹코드는 필수입니다.")
  @Size(max = 20, message = "그룹코드는 20자 이하로 입력해주세요.")
  private String groupCode;

  @NotBlank(message = "그룹명은 필수입니다.")
  @Size(max = 50, message = "그룹명은 50자 이하로 입력해주세요.")
  private String groupName;

  private String description;

  @NotNull(message = "업무 유형은 필수입니다.")
  private CodeGroupDomain domain;

  @NotNull(message = "사용여부는 필수입니다.")
  private Boolean enabled;

}
