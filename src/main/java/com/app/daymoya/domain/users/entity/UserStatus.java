package com.app.daymoya.domain.users.entity;

import lombok.Getter;

@Getter
public enum UserStatus {

  ACTIVE("활성"),
  WITHDRAWN("탈퇴"),
  SUSPENDED("정지");

  private final String label;

  UserStatus(String label){
    this.label = label;
  }

}
