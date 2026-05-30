package com.app.daymoya.domain.users.service;

import com.app.daymoya.domain.users.dto.request.UpdateProfileRequest;
import com.app.daymoya.domain.users.entity.User;
import com.app.daymoya.domain.users.repository.UserRepository;
import com.app.daymoya.global.exception.BizException;
import com.app.daymoya.global.file.FileDirectory;
import com.app.daymoya.global.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.app.daymoya.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final FileService fileService;

  /** 프로필 변경 */
  @Transactional
  public void updateProfile(Long userId, UpdateProfileRequest request) {

    // 회원 조회
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new BizException(USER_NOT_FOUND));

    String oldPath = user.getProfileImagePath();
    String newPath = null;

    // 프로필 이미지 경로 추출
    if (request.hasProfileImage()) {
      newPath = fileService.saveFile(request.getProfileImage(), FileDirectory.PROFILE);
    }

    // 프로필 변경
    user.updateProfile(request.getNickname(), newPath);

    // 기존 프로필 이미지 삭제
    if (StringUtils.hasText(oldPath) &&
      request.hasProfileImage() &&
      !oldPath.startsWith("profile/default-profile") &&
      !oldPath.startsWith("profile/withdrawn-profile")) {
      fileService.deleteFile(oldPath);
    }
  }

}
