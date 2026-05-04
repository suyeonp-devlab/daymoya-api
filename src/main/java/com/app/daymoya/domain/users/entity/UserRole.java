package com.app.daymoya.domain.users.entity;

import lombok.Getter;

@Getter
public enum UserRole {

  ADMIN("관리자"),
  USER("일반사용자");

  private final String label;

  UserRole(String label) {
    this.label = label;
  }

}
