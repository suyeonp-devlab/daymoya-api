package com.app.daymoya.domain.codes.dto.response;

import com.app.daymoya.domain.codes.entity.CodeGroup;
import com.app.daymoya.domain.codes.entity.CodeGroupDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CodeGroupResponse {

  private Long groupId;               // 그룹코드 PK
  private String groupCode;           // 그룹코드
  private String groupName;           // 그룹명
  private String description;         // 그룹설명
  private CodeGroupDomain domain;     // 업무(코드)
  private String domainNm;            // 업무(명)
  private Boolean enabled;            // 사용여부
  private List<CodeResponse> codes;   // 코드배열

  public static CodeGroupResponse from(CodeGroup codeGroup) {

    return CodeGroupResponse.builder()
      .groupId(codeGroup.getId())
      .groupCode(codeGroup.getGroupCode())
      .groupName(codeGroup.getGroupName())
      .description(codeGroup.getDescription())
      .domain(codeGroup.getDomain())
      .domainNm(codeGroup.getDomain().getLabel())
      .enabled(codeGroup.getEnabled())
      .build();
  }

  public static CodeGroupResponse from(CodeGroup codeGroup, List<CodeResponse> codes) {
    CodeGroupResponse response = from(codeGroup);
    response.codes = codes;
    return response;
  }

}
