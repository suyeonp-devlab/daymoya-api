package com.app.daymoya.domain.codes.service;

import com.app.daymoya.domain.codes.dto.request.*;
import com.app.daymoya.domain.codes.dto.response.CodeGroupResponse;
import com.app.daymoya.domain.codes.dto.response.CodeResponse;
import com.app.daymoya.domain.codes.entity.Code;
import com.app.daymoya.domain.codes.entity.CodeGroup;
import com.app.daymoya.domain.codes.repository.CodeGroupRepository;
import com.app.daymoya.domain.codes.repository.CodeRepository;
import com.app.daymoya.global.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app.daymoya.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

  private final CodeGroupRepository codeGroupRepository;
  private final CodeRepository codeRepository;

  /** 그룹코드 목록 조회 */
  public List<CodeGroupResponse> getAllCodeGroups() {

    return codeGroupRepository.findAll().stream()
      .map(CodeGroupResponse::from)
      .toList();
  }

  /** 그룹코드 생성 */
  @Transactional
  public void createCodeGroup(CodeGroupCreateRequest request) {

    String groupCode = request.getGroupCode().trim().toUpperCase();

    // 그룹코드 중복체크
    if (codeGroupRepository.existsByGroupCode(groupCode)) {
      throw new BizException(CODE_GROUP_ALREADY_EXISTS);
    }

    // 그룹코드 등록
    CodeGroup codeGroup = CodeGroup.create(
      groupCode,
      request.getGroupName(),
      request.getDescription(),
      request.getDomain(),
      request.getEnabled()
    );

    codeGroupRepository.save(codeGroup);
  }

  /** 그룹코드 수정 */
  @Transactional
  public void updateCodeGroup(Long groupId, CodeGroupUpdateRequest request) {

    // 그룹코드 조회
    CodeGroup codeGroup = codeGroupRepository.findById(groupId)
      .orElseThrow(() -> new BizException(CODE_GROUP_NOT_FOUND));

    // 그룹코드 수정
    codeGroup.update(
      request.getGroupName(),
      request.getDescription(),
      request.getDomain(),
      request.getEnabled()
    );
  }

  /** 코드 목록 조회 */
  public CodeGroupResponse getAllCodes(Long groupId, Boolean enabled) {

    // 그룹코드 조회
    CodeGroup codeGroup = codeGroupRepository.findById(groupId)
      .orElseThrow(() -> new BizException(CODE_GROUP_NOT_FOUND));

    // 코드 조회
    List<CodeResponse> codes = codeRepository.findAllByGroupIdOrderBySortNoAsc(groupId).stream()
      .filter((entity) -> enabled == null || entity.getEnabled().equals(enabled))
      .map(CodeResponse::from)
      .toList();

    return CodeGroupResponse.from(codeGroup, codes);
  }

  /** 코드 생성 */
  @Transactional
  public void createCode(Long groupId, CodeCreateRequest request) {

    String reqCode = request.getCode().trim().toUpperCase();

    // 그룹코드 조회
    if (!codeGroupRepository.existsById(groupId)) {
      throw new BizException(CODE_GROUP_NOT_FOUND);
    }

    // 코드 중복체크
    if (codeRepository.existsByGroupIdAndCode(groupId, reqCode)) {
      throw new BizException(CODE_ALREADY_EXISTS);
    }

    // 코드 등록
    Code code = Code.create(
      groupId,
      reqCode,
      request.getCodeName(),
      request.getDescription(),
      request.getEtc1(),
      request.getEtc2(),
      request.getEtc3(),
      request.getEnabled(),
      request.getSortNo()
    );

    codeRepository.save(code);
  }

  /** 코드 수정 */
  @Transactional
  public void updateCode(Long groupId, Long codeId, CodeUpdateRequest request) {

    // 코드 조회
    Code code = codeRepository.findById(codeId)
      .orElseThrow(() -> new BizException(CODE_NOT_FOUND));

    // 그룹코드 일치여부
    if (!groupId.equals(code.getGroupId())) {
      throw new BizException(CODE_NOT_FOUND);
    }

    // 코드 수정
    code.update(
      request.getCodeName(),
      request.getDescription(),
      request.getEtc1(),
      request.getEtc2(),
      request.getEtc3(),
      request.getEnabled(),
      request.getSortNo()
    );
  }

}
