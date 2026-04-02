package com.app.daymoya.domain.common.code.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CodeResponse {

  private String grpCodeId;
  private String grpCodeName;
  private List<CodeItem> codes;

  @Getter
  @AllArgsConstructor
  public static class CodeItem {
    private String code;
    private String codeName;
    private Integer sortOrder;
  }

}
