package com.app.daymoya.domain.users.entity;

import lombok.Getter;

@Getter
public enum UserStatus {

  ACTIVE("활성"),
  SUSPENDED("정지"),
  WITHDRAWN("탈퇴");

  private final String label;

  UserStatus(String label) {
    this.label = label;
  }

}
