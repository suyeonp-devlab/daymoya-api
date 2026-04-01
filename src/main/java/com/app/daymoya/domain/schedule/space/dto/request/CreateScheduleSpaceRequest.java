package com.app.daymoya.domain.schedule.space.dto.request;

import com.app.daymoya.domain.schedule.space.entity.SpaceColor;
import com.app.daymoya.domain.schedule.space.entity.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateScheduleSpaceRequest {

  @NotBlank(message = "스케줄 공간명은 필수입니다.")
  @Size(max = 50, message = "스케줄 공간명은 50자 이하로 입력해주세요.")
  private String name;

  @Size(max = 200, message = "스케줄 공간 설명은 200자 이하로 입력해주세요.")
  private String description;

  @NotNull(message = "스케줄 공간 유형은 필수입니다.")
  private SpaceType spaceType;

  private SpaceColor spaceColor;

}
