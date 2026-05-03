package com.app.daymoya.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

  /** =====================================
   * 공통
   ========================================= */
  INVALID_VALUE(BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
  SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON-500", "서버 내부 오류가 발생했습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-401", "인증이 필요합니다."),
  ACCESS_DENIED(FORBIDDEN, "COMMON-403", "접근 권한이 없습니다."),
  MANY_REQUEST(TOO_MANY_REQUESTS, "COMMON-429", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

  /** =====================================
   * AUTH 관련
   ========================================= */
  INVALID_SECURITY_CONTEXT(INTERNAL_SERVER_ERROR, "AUTH-001", "인증 컨텍스트가 올바르지 않습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
