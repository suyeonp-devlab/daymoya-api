package com.app.daymoya.domain.common.code.mapper;

import com.app.daymoya.domain.common.code.dto.response.CodeResponse;
import com.app.daymoya.domain.common.code.entity.Code;
import com.app.daymoya.domain.common.code.entity.GrpCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CodeMapper {

  /** GrpCode + Code → CodeResponse */
  public CodeResponse toCodeResponse(GrpCode grpCode, List<Code> codes) {

    List<CodeResponse.CodeItem > codeItems = codes.stream().map(this::toCodeItem).toList();

    return new CodeResponse(
      grpCode.getGrpCodeId(),
      grpCode.getGrpCodeName(),
      codeItems
    );
  }

  /** Code → CodeItem */
  private CodeResponse.CodeItem toCodeItem(Code code) {
    return new CodeResponse.CodeItem(
      code.getCode(),
      code.getCodeName(),
      code.getSortOrder()
    );
  }

}
