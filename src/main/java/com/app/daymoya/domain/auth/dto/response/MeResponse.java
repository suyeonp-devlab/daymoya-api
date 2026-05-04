package com.app.daymoya.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeResponse {

  private Long userId;             // 사용자 PK
  private String maskedEmail;      // 이메일
  private String nickname;         // 닉네임
  private String profileImageUrl;  // 프로필 이미지 Url

}
