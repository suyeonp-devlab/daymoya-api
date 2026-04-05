package com.app.daymoya.domain.schedule.space.dto.response;

import com.app.daymoya.domain.schedule.space.entity.SpaceColor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupSpaceResponse {

  private Long scheduleSpaceId;
  private String name;
  private String description;
  private SpaceColor spaceColor;

}
