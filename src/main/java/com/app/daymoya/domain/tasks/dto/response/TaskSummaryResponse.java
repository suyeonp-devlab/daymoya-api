package com.app.daymoya.domain.tasks.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskSummaryResponse {

  private int total;       // 전체 일정
  private int todo;        // 할일
  private int inProgress;  // 진행중
  private int done;        // 완료
  private int cancelled;   // 취소

}
