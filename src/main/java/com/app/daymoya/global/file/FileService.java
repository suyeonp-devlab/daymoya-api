package com.app.daymoya.global.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  /** 파일 저장 */
  String saveFile(MultipartFile file, FileDirectory directory);

  /** 파일 삭제 */
  void deleteFile(String relativePath);

  /** 파일 접근 url 생성 */
  String buildFileUrl(String relativePath);

}
