package com.app.daymoya.global.file;

import lombok.Getter;

import java.util.Set;

@Getter
public enum FileDirectory {

  PROFILE("profile", 2 * 1024 * 1024, Set.of("jpg", "jpeg", "png", "webp"));

  private final String path;              // 하위경로
  private final long maxSize;             // 최대사이즈
  private final Set<String> allowedExts;  // 허용 확장자

  FileDirectory(String path, long maxSize, Set<String> allowedExts) {
    this.path = path;
    this.maxSize = maxSize;
    this.allowedExts = allowedExts;
  }

}
