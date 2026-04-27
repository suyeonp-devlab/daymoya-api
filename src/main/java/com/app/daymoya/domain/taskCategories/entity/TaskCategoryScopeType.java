package com.app.daymoya.domain.taskCategories.entity;

import lombok.Getter;

@Getter
public enum TaskCategoryScopeType {

  SYSTEM_PERSONAL("개인 기본 카테고리"),
  PERSONAL("개인 커스텀 카테고리"),
  SYSTEM_GROUP("그룹 기본 카테고리"),
  GROUP("그룹 커스텀 카테고리");

  private final String label;

  TaskCategoryScopeType(String label) {
    this.label = label;
  }

}
