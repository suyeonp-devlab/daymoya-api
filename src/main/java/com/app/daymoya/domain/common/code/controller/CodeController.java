package com.app.daymoya.domain.common.code.controller;

import com.app.daymoya.domain.common.code.dto.request.CodeRequest;
import com.app.daymoya.domain.common.code.dto.response.CodeResponse;
import com.app.daymoya.domain.common.code.service.CodeService;
import com.app.daymoya.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController {

  private final CodeService codeService;

  /** 공통코드 조회 */
  @PostMapping
  public ApiResponse<CodeResponse> getCode(@Valid @RequestBody CodeRequest request) {

    CodeResponse response = codeService.getCode(request.getGrpCodeId(), request.getCode());
    return ApiResponse.success(response);
  }

}
