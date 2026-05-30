package com.app.daymoya.domain.users.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileRequest {

  @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
  private String nickname;

  private MultipartFile profileImage;

  public boolean hasProfileImage() {
    return profileImage != null && !profileImage.isEmpty();
  }

}
