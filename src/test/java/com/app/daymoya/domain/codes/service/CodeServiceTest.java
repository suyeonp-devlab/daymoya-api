package com.app.daymoya.domain.codes.service;

import com.app.daymoya.domain.codes.dto.request.CodeCreateRequest;
import com.app.daymoya.domain.codes.dto.request.CodeGroupCreateRequest;
import com.app.daymoya.domain.codes.dto.request.CodeGroupUpdateRequest;
import com.app.daymoya.domain.codes.dto.request.CodeUpdateRequest;
import com.app.daymoya.domain.codes.dto.response.CodeGroupResponse;
import com.app.daymoya.domain.codes.entity.Code;
import com.app.daymoya.domain.codes.entity.CodeGroup;
import com.app.daymoya.domain.codes.entity.CodeGroupDomain;
import com.app.daymoya.domain.codes.repository.CodeGroupRepository;
import com.app.daymoya.domain.codes.repository.CodeRepository;
import com.app.daymoya.global.exception.BizException;
import com.app.daymoya.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

/** CodeService 단위 테스트 */
@DisplayName("CodeService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CodeServiceTest {

  @Mock
  private CodeGroupRepository codeGroupRepository;

  @Mock
  private CodeRepository codeRepository;

  @InjectMocks
  private CodeService codeService;

  @Nested
  @DisplayName("getAllCodeGroups - 그룹코드 목록 조회")
  class GetAllCodeGroups {

    @Test
    @DisplayName("성공 - 전체 그룹코드 목록을 반환한다")
    void success() {
      // given
      CodeGroup codeGroup1 = createCodeGroup(1L, "TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      CodeGroup codeGroup2 = createCodeGroup(2L, "USER_ROLE", "사용자 역할", CodeGroupDomain.USER, false);
      given(codeGroupRepository.findAll()).willReturn(List.of(codeGroup1, codeGroup2));

      // when
      List<CodeGroupResponse> result = codeService.getAllCodeGroups();

      // then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getGroupCode()).isEqualTo("TASK_STATUS");
      assertThat(result.get(1).getGroupCode()).isEqualTo("USER_ROLE");
    }

    @Test
    @DisplayName("성공 - 그룹코드가 없으면 빈 목록을 반환한다")
    void success_emptyList() {
      // given
      given(codeGroupRepository.findAll()).willReturn(List.of());

      // when
      List<CodeGroupResponse> result = codeService.getAllCodeGroups();

      // then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("createCodeGroup - 그룹코드 생성")
  class CreateCodeGroup {

    @Test
    @DisplayName("성공 - 그룹코드를 생성하고 저장한다")
    void success() {
      // given
      CodeGroupCreateRequest request = createCodeGroupCreateRequest("TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      given(codeGroupRepository.existsByGroupCode("TASK_STATUS")).willReturn(false);

      // when
      codeService.createCodeGroup(request);

      // then
      then(codeGroupRepository).should(times(1)).save(any(CodeGroup.class));
    }

    @Test
    @DisplayName("성공 - groupCode는 소문자 입력이어도 대문자로 변환되어 저장된다")
    void success_groupCodeToUpperCase() {
      // given
      CodeGroupCreateRequest request = createCodeGroupCreateRequest("task_status", "일정 상태", CodeGroupDomain.TASK, true);
      given(codeGroupRepository.existsByGroupCode("TASK_STATUS")).willReturn(false);

      ArgumentCaptor<CodeGroup> captor = ArgumentCaptor.forClass(CodeGroup.class);

      // when
      codeService.createCodeGroup(request);

      // then
      then(codeGroupRepository).should().save(captor.capture());
      CodeGroup savedCodeGroup = captor.getValue();
      assertThat(savedCodeGroup.getGroupCode()).isEqualTo("TASK_STATUS");
    }

    @Test
    @DisplayName("실패 - 이미 존재하는 그룹코드면 예외가 발생하고 저장하지 않는다")
    void fail_duplicateGroupCode() {
      // given
      CodeGroupCreateRequest request = createCodeGroupCreateRequest("TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      given(codeGroupRepository.existsByGroupCode("TASK_STATUS")).willReturn(true);

      // when & then
      assertThatThrownBy(() -> codeService.createCodeGroup(request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_GROUP_ALREADY_EXISTS);

      then(codeGroupRepository).should(never()).save(any());
    }
  }

  @Nested
  @DisplayName("updateCodeGroup - 그룹코드 수정")
  class UpdateCodeGroup {

    @Test
    @DisplayName("성공 - 그룹코드 정보를 수정한다")
    void success() {
      // given
      CodeGroup codeGroup = createCodeGroup(1L, "TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      CodeGroupUpdateRequest request = createCodeGroupUpdateRequest("수정된 일정 상태", CodeGroupDomain.CMN, false);
      given(codeGroupRepository.findById(1L)).willReturn(Optional.of(codeGroup));

      // when
      codeService.updateCodeGroup(1L, request);

      // then
      assertThat(codeGroup.getGroupName()).isEqualTo("수정된 일정 상태");
      assertThat(codeGroup.getDomain()).isEqualTo(CodeGroupDomain.CMN);
      assertThat(codeGroup.getEnabled()).isFalse();
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 그룹코드 ID면 예외가 발생한다")
    void fail_codeGroupNotFound() {
      // given
      CodeGroupUpdateRequest request = createCodeGroupUpdateRequest("수정된 이름", CodeGroupDomain.CMN, true);
      given(codeGroupRepository.findById(999L)).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> codeService.updateCodeGroup(999L, request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_GROUP_NOT_FOUND);
    }
  }

  @Nested
  @DisplayName("getAllCodes - 코드 목록 조회")
  class GetAllCodes {

    @Test
    @DisplayName("성공 - enabled 파라미터 없이 전체 코드를 반환한다")
    void success_noFilter() {
      // given
      CodeGroup codeGroup = createCodeGroup(1L, "TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      Code activeCode   = createCode(1L, 1L, "TODO", "할 일", true, 1);
      Code inactiveCode = createCode(2L, 1L, "DONE", "완료", false, 2);

      given(codeGroupRepository.findById(1L)).willReturn(Optional.of(codeGroup));
      given(codeRepository.findAllByGroupIdOrderBySortNoAsc(1L)).willReturn(List.of(activeCode, inactiveCode));

      // when
      CodeGroupResponse result = codeService.getAllCodes(1L, null);

      // then
      assertThat(result.getCodes()).hasSize(2);
    }

    @Test
    @DisplayName("성공 - enabled=true 조회 시 활성화된 코드만 반환한다")
    void success_filterEnabled() {
      // given
      CodeGroup codeGroup = createCodeGroup(1L, "TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      Code activeCode   = createCode(1L, 1L, "TODO", "할 일", true, 1);
      Code inactiveCode = createCode(2L, 1L, "DONE", "완료", false, 2);

      given(codeGroupRepository.findById(1L)).willReturn(Optional.of(codeGroup));
      given(codeRepository.findAllByGroupIdOrderBySortNoAsc(1L)).willReturn(List.of(activeCode, inactiveCode));

      // when
      CodeGroupResponse result = codeService.getAllCodes(1L, true);

      // then
      assertThat(result.getCodes()).hasSize(1);
      assertThat(result.getCodes().get(0).getCode()).isEqualTo("TODO");
    }

    @Test
    @DisplayName("성공 - enabled=false 조회 시 비활성화된 코드만 반환한다")
    void success_filterDisabled() {
      // given
      CodeGroup codeGroup = createCodeGroup(1L, "TASK_STATUS", "일정 상태", CodeGroupDomain.TASK, true);
      Code activeCode   = createCode(1L, 1L, "TODO", "할 일", true, 1);
      Code inactiveCode = createCode(2L, 1L, "DONE", "완료", false, 2);

      given(codeGroupRepository.findById(1L)).willReturn(Optional.of(codeGroup));
      given(codeRepository.findAllByGroupIdOrderBySortNoAsc(1L)).willReturn(List.of(activeCode, inactiveCode));

      // when
      CodeGroupResponse result = codeService.getAllCodes(1L, false);

      // then
      assertThat(result.getCodes()).hasSize(1);
      assertThat(result.getCodes().get(0).getCode()).isEqualTo("DONE");
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 그룹코드 ID면 예외가 발생한다")
    void fail_codeGroupNotFound() {
      // given
      given(codeGroupRepository.findById(999L)).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> codeService.getAllCodes(999L, null))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_GROUP_NOT_FOUND);
    }
  }

  @Nested
  @DisplayName("createCode - 코드 생성")
  class CreateCode {

    @Test
    @DisplayName("성공 - 코드를 생성하고 저장한다")
    void success() {
      // given
      CodeCreateRequest request = createCodeCreateRequest("TODO", "할 일", true, 1);
      given(codeGroupRepository.existsById(1L)).willReturn(true);
      given(codeRepository.existsByGroupIdAndCode(1L, "TODO")).willReturn(false);

      // when
      codeService.createCode(1L, request);

      // then
      then(codeRepository).should(times(1)).save(any(Code.class));
    }

    @Test
    @DisplayName("성공 - code는 소문자 입력이어도 대문자로 변환되어 저장된다")
    void success_codeToUpperCase() {
      // given
      CodeCreateRequest request = createCodeCreateRequest("todo", "할 일", true, 1);
      given(codeGroupRepository.existsById(1L)).willReturn(true);
      given(codeRepository.existsByGroupIdAndCode(1L, "TODO")).willReturn(false);

      ArgumentCaptor<Code> captor = ArgumentCaptor.forClass(Code.class);

      // when
      codeService.createCode(1L, request);

      // then
      then(codeRepository).should().save(captor.capture());
      Code savedCode = captor.getValue();
      assertThat(savedCode.getCode()).isEqualTo("TODO");
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 그룹코드 ID면 예외가 발생하고 저장하지 않는다")
    void fail_codeGroupNotFound() {
      // given
      CodeCreateRequest request = createCodeCreateRequest("TODO", "할 일", true, 1);
      given(codeGroupRepository.existsById(999L)).willReturn(false);

      // when & then
      assertThatThrownBy(() -> codeService.createCode(999L, request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_GROUP_NOT_FOUND);

      then(codeRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("실패 - 동일 그룹 내 이미 존재하는 코드면 예외가 발생하고 저장하지 않는다")
    void fail_duplicateCode() {
      // given
      CodeCreateRequest request = createCodeCreateRequest("TODO", "할 일", true, 1);
      given(codeGroupRepository.existsById(1L)).willReturn(true);
      given(codeRepository.existsByGroupIdAndCode(1L, "TODO")).willReturn(true);

      // when & then
      assertThatThrownBy(() -> codeService.createCode(1L, request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_ALREADY_EXISTS);

      then(codeRepository).should(never()).save(any());
    }
  }

  @Nested
  @DisplayName("updateCode - 코드 수정")
  class UpdateCode {

    @Test
    @DisplayName("성공 - 코드 정보를 수정한다")
    void success() {
      // given
      Code code = createCode(1L, 1L, "TODO", "할 일", true, 1);
      CodeUpdateRequest request = createCodeUpdateRequest("수정된 할 일", false, 2);
      given(codeRepository.findById(1L)).willReturn(Optional.of(code));

      // when
      codeService.updateCode(1L, 1L, request);

      // then
      assertThat(code.getCodeName()).isEqualTo("수정된 할 일");
      assertThat(code.getEnabled()).isFalse();
      assertThat(code.getSortNo()).isEqualTo(2);
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 코드 ID면 예외가 발생한다")
    void fail_codeNotFound() {
      // given
      CodeUpdateRequest request = createCodeUpdateRequest("수정된 할 일", true, 1);
      given(codeRepository.findById(999L)).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> codeService.updateCode(1L, 999L, request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_NOT_FOUND);
    }

    @Test
    @DisplayName("실패 - 코드가 속한 그룹코드 ID와 경로의 그룹코드 ID가 다르면 예외가 발생한다")
    void fail_groupIdMismatch() {
      // given
      Code code = createCode(1L, 1L, "TODO", "할 일", true, 1);
      CodeUpdateRequest request = createCodeUpdateRequest("수정된 할 일", true, 1);
      given(codeRepository.findById(1L)).willReturn(Optional.of(code));

      // when & then
      assertThatThrownBy(() -> codeService.updateCode(99L, 1L, request))
        .isInstanceOf(BizException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.CODE_NOT_FOUND);
    }
  }

  /** 테스트용 CodeGroup 생성 */
  private CodeGroup createCodeGroup(Long id, String groupCode, String groupName, CodeGroupDomain domain, Boolean enabled) {
    CodeGroup codeGroup = CodeGroup.create(groupCode, groupName, null, domain, enabled);
    ReflectionTestUtils.setField(codeGroup, "id", id);
    return codeGroup;
  }

  /** 테스트용 Code 생성 */
  private Code createCode(Long id, Long groupId, String code, String codeName, Boolean enabled, Integer sortNo) {
    Code c = Code.create(groupId, code, codeName, null, null, null, null, enabled, sortNo);
    ReflectionTestUtils.setField(c, "id", id);
    return c;
  }

  /** 테스트용 CodeGroupCreateRequest 생성 */
  private CodeGroupCreateRequest createCodeGroupCreateRequest(String groupCode, String groupName, CodeGroupDomain domain, Boolean enabled) {
    CodeGroupCreateRequest request = new CodeGroupCreateRequest();
    ReflectionTestUtils.setField(request, "groupCode", groupCode);
    ReflectionTestUtils.setField(request, "groupName", groupName);
    ReflectionTestUtils.setField(request, "domain", domain);
    ReflectionTestUtils.setField(request, "enabled", enabled);
    return request;
  }

  /** 테스트용 CodeGroupUpdateRequest 생성 */
  private CodeGroupUpdateRequest createCodeGroupUpdateRequest(String groupName, CodeGroupDomain domain, Boolean enabled) {
    CodeGroupUpdateRequest request = new CodeGroupUpdateRequest();
    ReflectionTestUtils.setField(request, "groupName", groupName);
    ReflectionTestUtils.setField(request, "domain", domain);
    ReflectionTestUtils.setField(request, "enabled", enabled);
    return request;
  }

  /** 테스트용 CodeCreateRequest 생성 */
  private CodeCreateRequest createCodeCreateRequest(String code, String codeName, Boolean enabled, Integer sortNo) {
    CodeCreateRequest request = new CodeCreateRequest();
    ReflectionTestUtils.setField(request, "code", code);
    ReflectionTestUtils.setField(request, "codeName", codeName);
    ReflectionTestUtils.setField(request, "enabled", enabled);
    ReflectionTestUtils.setField(request, "sortNo", sortNo);
    return request;
  }

  /** 테스트용 CodeUpdateRequest 생성 */
  private CodeUpdateRequest createCodeUpdateRequest(String codeName, Boolean enabled, Integer sortNo) {
    CodeUpdateRequest request = new CodeUpdateRequest();
    ReflectionTestUtils.setField(request, "codeName", codeName);
    ReflectionTestUtils.setField(request, "enabled", enabled);
    ReflectionTestUtils.setField(request, "sortNo", sortNo);
    return request;
  }

}
