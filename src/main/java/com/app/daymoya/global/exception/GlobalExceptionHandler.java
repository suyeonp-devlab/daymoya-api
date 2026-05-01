package com.app.daymoya.global.exception;

import com.app.daymoya.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /** 커스텀 bizException 예외 응답 처리 */
  @ExceptionHandler(BizException.class)
  public ResponseEntity<ApiResponse<Void>> handleBizException(BizException e) {

    ErrorCode errorCode = e.getErrorCode();

    return ResponseEntity
      .status(errorCode.getStatus())
      .body(ApiResponse.fail(errorCode.getCode(), e.getMessage())); // 커스텀 메세지 고려
  }

  /** @Valid 예외 응답 처리 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {

    ErrorCode errorCode = ErrorCode.INVALID_VALUE;

    String message = e.getBindingResult().getFieldErrors().isEmpty()
      ? errorCode.getMessage()
      : e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

    return ResponseEntity
      .status(errorCode.getStatus())
      .body(ApiResponse.fail(errorCode.getCode(), message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

    ErrorCode errorCode = ErrorCode.SERVER_ERROR;

    return ResponseEntity
      .status(errorCode.getStatus())
      .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
  }

}
