package com.app.daymoya.domain.schedule.space.dto.response;

import com.app.daymoya.domain.schedule.space.entity.SpaceColor;
import com.app.daymoya.domain.schedule.space.entity.SpaceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleSpaceResponse {

  private Long scheduleSpaceId;
  private String name;
  private String description;
  private SpaceType spaceType;
  private SpaceColor spaceColor;

}
