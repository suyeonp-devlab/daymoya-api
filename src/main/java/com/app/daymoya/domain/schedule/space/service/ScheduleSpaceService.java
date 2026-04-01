package com.app.daymoya.domain.schedule.space.service;

import com.app.daymoya.domain.schedule.member.entity.ScheduleSpaceMember;
import com.app.daymoya.domain.schedule.member.entity.SpaceMemberRole;
import com.app.daymoya.domain.schedule.member.repository.ScheduleSpaceMemberRepository;
import com.app.daymoya.domain.schedule.space.dto.request.CreateScheduleSpaceRequest;
import com.app.daymoya.domain.schedule.space.dto.response.CreateScheduleSpaceResponse;
import com.app.daymoya.domain.schedule.space.entity.ScheduleSpace;
import com.app.daymoya.domain.schedule.space.entity.SpaceColor;
import com.app.daymoya.domain.schedule.space.entity.SpaceType;
import com.app.daymoya.domain.schedule.space.repository.ScheduleSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleSpaceService {

  private final ScheduleSpaceRepository scheduleSpaceRepository;
  private final ScheduleSpaceMemberRepository scheduleSpaceMemberRepository;

  /** (사용자 요청 기반) 스케줄 공간 생성 */
  @Transactional
  public CreateScheduleSpaceResponse create(CreateScheduleSpaceRequest request, Long memberId) {

    // default 공간 색상 설정
    SpaceColor spaceColor = request.getSpaceColor() != null ? request.getSpaceColor() : SpaceColor.SKY;

    // 스케줄 공간 생성
    Long scheduleSpaceId = saveSpaceWithOwner(
      memberId
     ,request.getName()
     ,request.getDescription()
     ,request.getSpaceType()
     ,spaceColor);

    return new CreateScheduleSpaceResponse(scheduleSpaceId);
  }

  /** (회원가입 시) 기본 개인 스케줄 공간 생성 */
  @Transactional
  public void createDefaultPersonalSpace(Long memberId) {
    saveSpaceWithOwner(memberId, "기본 일정", null, SpaceType.PERSONAL, SpaceColor.SKY);
  }

  /** 스케줄 공간 저장 및 소유자 멤버 지정 */
  private Long saveSpaceWithOwner(Long memberId
                                 ,String name
                                 ,String description
                                 ,SpaceType spaceType
                                 ,SpaceColor spaceColor) {

    LocalDateTime now = LocalDateTime.now();

    // 스케줄 공간 등록
    ScheduleSpace scheduleSpace = ScheduleSpace.create(
      name
     ,description
     ,spaceType
     ,spaceColor);

    scheduleSpaceRepository.save(scheduleSpace);

    // 스케줄 공간 회원 등록
    ScheduleSpaceMember owner = ScheduleSpaceMember.create(
      scheduleSpace.getId()
     ,memberId
     ,SpaceMemberRole.OWNER
     ,now
    );

    scheduleSpaceMemberRepository.save(owner);

    return scheduleSpace.getId();
  }

}
