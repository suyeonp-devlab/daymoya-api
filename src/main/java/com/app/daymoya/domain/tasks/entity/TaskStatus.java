package com.app.daymoya.domain.tasks.entity;

import lombok.Getter;

@Getter
public enum TaskStatus {

  TODO("할일"),
  IN_PROGRESS("진행중"),
  DONE("완료"),
  CANCELLED("취소");

  private final String label;

  TaskStatus(String label) {
    this.label = label;
  }

}
