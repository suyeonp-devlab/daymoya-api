package com.app.groupmissionapi.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

  private final boolean success;
  private final String code;
  private final String message;
  private final T data;

  private ApiResponse(boolean success, String code, String message, T data) {
    this.success = success;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, "COMMON-200", "요청이 성공했습니다.", data);
  }

  public static ApiResponse<Void> success() {
    return new ApiResponse<>(true, "COMMON-200", "요청이 성공했습니다.", null);
  }

  public static ApiResponse<Void> fail(String code, String message) {
    return new ApiResponse<>(false, code, message, null);
  }

}
