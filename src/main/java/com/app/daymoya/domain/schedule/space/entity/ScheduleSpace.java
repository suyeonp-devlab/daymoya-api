package com.app.daymoya.domain.schedule.space.entity;

import com.app.daymoya.domain.member.entity.Member;
import com.app.daymoya.domain.member.entity.MemberStatus;
import com.app.daymoya.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "schedule_spaces")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleSpace extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 스케줄 공간 이름
  @Column(nullable = false, length = 50)
  private String name;

  // 스케줄 공간 설명
  private String description;

  // 스케줄 공간 유형 (PERSONAL 개인, GROUP 그룹)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SpaceType spaceType;

  // 스케줄 공간 색상 (HEX 코드)
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private SpaceColor spaceColor;

  // 스케줄 공간 상태 (ACTIVE 활성, DELETED 삭제)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SpaceStatus status;

  /** 스케줄 공간 생성 */
  public static ScheduleSpace create(String name
                                    ,String description
                                    ,SpaceType spaceType
                                    ,SpaceColor spaceColor) {

    return ScheduleSpace.builder()
      .name(name)
      .description(description)
      .spaceType(spaceType)
      .spaceColor(spaceColor)
      .status(SpaceStatus.ACTIVE)
      .build();
  }

}
