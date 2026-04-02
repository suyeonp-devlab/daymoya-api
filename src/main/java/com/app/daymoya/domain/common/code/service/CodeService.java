package com.app.daymoya.domain.common.code.service;

import com.app.daymoya.domain.common.code.dto.response.CodeResponse;
import com.app.daymoya.domain.common.code.entity.Code;
import com.app.daymoya.domain.common.code.entity.CodeUseYn;
import com.app.daymoya.domain.common.code.entity.GrpCode;
import com.app.daymoya.domain.common.code.mapper.CodeMapper;
import com.app.daymoya.domain.common.code.repository.CodeRepository;
import com.app.daymoya.domain.common.code.repository.GrpCodeRepository;
import com.app.daymoya.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.app.daymoya.global.exception.ErrorCode.CODE_INVALID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

  private final CodeMapper codeMapper;
  private final GrpCodeRepository grpCodeRepository;
  private final CodeRepository codeRepository;

  /** 공통코드 조회 */
  public CodeResponse getCode(String grpCodeId, String code) {

    // 공통코드 그룹 조회
    GrpCode grpCode = grpCodeRepository.findByGrpCodeIdAndUseYn(grpCodeId, CodeUseYn.Y)
      .orElseThrow(() -> new CustomException(CODE_INVALID));

    // 상세코드 조회
    List<Code> codes = null;

    if (StringUtils.hasText(code)) {

      codes = codeRepository.findByGrpCodeIdAndCodeAndUseYn(grpCodeId, code, CodeUseYn.Y)
        .map(List::of)
        .orElseThrow(() -> new CustomException(CODE_INVALID));

    } else {
      codes = codeRepository.findByGrpCodeIdAndUseYnOrderBySortOrderAsc(grpCodeId, CodeUseYn.Y);
    }

    return codeMapper.toCodeResponse(grpCode, codes);
  }

}
