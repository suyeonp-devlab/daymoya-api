package com.app.daymoya.domain.codes.controller;

import com.app.daymoya.domain.codes.dto.request.*;
import com.app.daymoya.domain.codes.dto.response.CodeGroupResponse;
import com.app.daymoya.domain.codes.service.CodeService;
import com.app.daymoya.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codes")
public class CodeController {

  private final CodeService codeService;

  /** 그룹코드 목록 조회 */
  @GetMapping("/groups")
  public ApiResponse<List<CodeGroupResponse>> getAllCodeGroups() {
    List<CodeGroupResponse> codeGroups = codeService.getAllCodeGroups();
    return ApiResponse.success(codeGroups);
  }

  /** 그룹코드 생성 */
  @PostMapping("/admin/groups")
  public ApiResponse<Void> createCodeGroup(@Valid @RequestBody CodeGroupCreateRequest request) {
    codeService.createCodeGroup(request);
    return ApiResponse.success();
  }

  /** 그룹코드 수정 */
  @PatchMapping("/admin/groups/{groupId}")
  public ApiResponse<Void> updateCodeGroup(@PathVariable Long groupId, @Valid @RequestBody CodeGroupUpdateRequest request) {
    codeService.updateCodeGroup(groupId, request);
    return ApiResponse.success();
  }

  /** 코드 목록 조회 */
  @GetMapping("/groups/{groupId}/codes")
  public ApiResponse<CodeGroupResponse> getAllCodes(@PathVariable Long groupId, @RequestParam(required = false) Boolean enabled) {
    CodeGroupResponse codeGroup = codeService.getAllCodes(groupId, enabled);
    return ApiResponse.success(codeGroup);
  }

  /** 코드 생성 */
  @PostMapping("/admin/groups/{groupId}/codes")
  public ApiResponse<Void> createCode(@PathVariable Long groupId, @Valid @RequestBody CodeCreateRequest request) {
    codeService.createCode(groupId, request);
    return ApiResponse.success();
  }

  /** 코드 수정 */
  @PatchMapping("/admin/groups/{groupId}/codes/{codeId}")
  public ApiResponse<Void> updateCode(
    @PathVariable Long groupId,
    @PathVariable Long codeId,
    @Valid @RequestBody CodeUpdateRequest request
  ) {
    codeService.updateCode(groupId, codeId, request);
    return ApiResponse.success();
  }

}
