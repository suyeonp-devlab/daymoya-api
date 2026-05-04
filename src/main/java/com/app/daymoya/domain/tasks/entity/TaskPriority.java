package com.app.daymoya.domain.tasks.entity;

import lombok.Getter;

@Getter
public enum TaskPriority {

  LOW("낮음"),
  NORMAL("보통"),
  HIGH("높음"),
  URGENT("긴급");

  private final String label;

  TaskPriority(String label) {
    this.label = label;
  }

}
