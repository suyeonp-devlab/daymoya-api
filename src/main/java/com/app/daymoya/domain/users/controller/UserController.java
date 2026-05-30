package com.app.daymoya.domain.users.controller;

import com.app.daymoya.domain.users.dto.request.UpdateProfileRequest;
import com.app.daymoya.domain.users.service.UserService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  /** 프로필 변경 */
  @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<Void> updateProfile(@CurrentUserId Long userId, @Valid @ModelAttribute UpdateProfileRequest request) {
    userService.updateProfile(userId, request);
    return ApiResponse.success();
  }

}
