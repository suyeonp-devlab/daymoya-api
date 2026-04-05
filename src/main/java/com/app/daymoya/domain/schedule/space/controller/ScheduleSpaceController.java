package com.app.daymoya.domain.schedule.space.controller;

import com.app.daymoya.domain.schedule.space.dto.request.CreateScheduleSpaceRequest;
import com.app.daymoya.domain.schedule.space.dto.response.CreateScheduleSpaceResponse;
import com.app.daymoya.domain.schedule.space.dto.response.GroupSpaceResponse;
import com.app.daymoya.domain.schedule.space.service.ScheduleSpaceService;
import com.app.daymoya.global.response.ApiResponse;
import com.app.daymoya.global.security.annotation.CurrentMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule/space")
public class ScheduleSpaceController {

  private final ScheduleSpaceService scheduleSpaceService;

  /** 스케줄 공간 생성 */
  @PostMapping("/create")
  public ApiResponse<CreateScheduleSpaceResponse> create(@Valid @RequestBody CreateScheduleSpaceRequest request
                                                        ,@CurrentMemberId Long memberId) {

    CreateScheduleSpaceResponse response = scheduleSpaceService.create(request, memberId);
    return ApiResponse.success(response);
  }

  /** 그룹 스케줄 공간 조회 */
  @PostMapping("/group")
  public ApiResponse<List<GroupSpaceResponse>> findGroupSpaces(@CurrentMemberId Long memberId) {

    List<GroupSpaceResponse> response = scheduleSpaceService.findGroupSpaces(memberId);
    return ApiResponse.success(response);
  }

}
