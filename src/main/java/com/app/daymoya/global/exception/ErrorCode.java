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
  INVALID_SECURITY_CONTEXT(INTERNAL_SERVER_ERROR, "AUTH-001", "인증 컨텍스트가 올바르지 않습니다."),

  /** =====================================
   * MAIL 관련
   ========================================= */
  MAIL_SEND_FAIL(INTERNAL_SERVER_ERROR, "MAIL-001", "이메일 발송에 실패했습니다."),

  /** =====================================
   * FILE(IMAGE) 관련
   ========================================= */
  FILE_EMPTY(BAD_REQUEST, "FILE-001", "파일이 비어있습니다."),
  INVALID_FILE_FORMAT(BAD_REQUEST, "FILE-002", "지원하지 않는 파일 형식입니다."),
  INVALID_FILE_SIZE(BAD_REQUEST, "FILE-003", "파일 용량이 허용 범위를 초과했습니다."),
  FILE_UPLOAD_FAIL(INTERNAL_SERVER_ERROR, "FILE-004", "파일 업로드 중 오류가 발생했습니다."),
  FILE_DELETE_FAIL(INTERNAL_SERVER_ERROR, "FILE-005", "파일 삭제 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
