package com.app.daymoya.domain.codes.entity;

import lombok.Getter;

@Getter
public enum CodeGroupDomain {

  CMN("공통"),
  USER("사용자"),
  TASK("할일"),
  CATEGORY("카테고리");

  private final String label;

  CodeGroupDomain(String label) {
    this.label = label;
  }

}
