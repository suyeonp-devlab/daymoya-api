package com.app.groupmissionapi.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // 공통
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "서버 내부 오류가 발생했습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-401", "인증이 필요합니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON-403", "접근 권한이 없습니다."),
  TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "COMMON-429", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

  // AUTH 관련
  EMAIL_OR_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "AUTH-001", "이메일 또는 비밀번호가 올바르지 않습니다."),
  MEMBER_LOCKED(HttpStatus.BAD_REQUEST, "AUTH-002", "잠긴 계정입니다."),
  PASSWORD_CHANGE_REQUIRED(HttpStatus.BAD_REQUEST, "AUTH-003", "비밀번호 변경이 필요합니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "AUTH-004", "이미 가입된 이메일입니다."),

  // MAIL 관련
  MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL-001", "이메일 발송에 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
