package com.app.daymoya.domain.categories.entity;

import lombok.Getter;

@Getter
public enum CategoryScope {

  SYS_PERSONAL("개인 기본 카테고리"),
  PERSONAL("개인 커스텀 카테고리"),
  SYS_GROUP("그룹 기본 카테고리"),
  GROUP("그룹 커스텀 카테고리");

  private final String label;

  CategoryScope(String label) {
    this.label = label;
  }

}
