package com.app.groupmissionapi.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

  private Long memberId;
  private String nickname;
  private String profileImageUrl;

}
