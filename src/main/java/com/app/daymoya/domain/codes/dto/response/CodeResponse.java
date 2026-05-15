package com.app.daymoya.domain.codes.dto.response;

import com.app.daymoya.domain.codes.entity.Code;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeResponse {

  private Long codeId;          // 코드 PK
  private Long groupId;         // 그룹코드 ID
  private String code;          // 코드
  private String codeName;      // 코드명
  private String description;   // 코드설명
  private String etc1;          // 기타1
  private String etc2;          // 기타2
  private String etc3;          // 기타3
  private Boolean enabled;      // 사용여부
  private Integer sortNo;       // 정렬순서

  public static CodeResponse from(Code code) {

    return CodeResponse.builder()
      .codeId(code.getId())
      .groupId(code.getGroupId())
      .code(code.getCode())
      .codeName(code.getCodeName())
      .description(code.getDescription())
      .etc1(code.getEtc1())
      .etc2(code.getEtc2())
      .etc3(code.getEtc3())
      .enabled(code.getEnabled())
      .sortNo(code.getSortNo())
      .build();
  }

}
