package com.app.daymoya.domain.schedule.space.repository;

import com.app.daymoya.domain.schedule.member.entity.QScheduleSpaceMember;
import com.app.daymoya.domain.schedule.member.entity.SpaceMemberStatus;
import com.app.daymoya.domain.schedule.space.dto.response.PersonaSpaceResponse;
import com.app.daymoya.domain.schedule.space.entity.QScheduleSpace;
import com.app.daymoya.domain.schedule.space.entity.SpaceStatus;
import com.app.daymoya.domain.schedule.space.entity.SpaceType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleSpaceQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  /** 스케줄 공간 조회 */
  public List<PersonaSpaceResponse> findScheduleSpaces(Long memberId, SpaceType spaceType) {

    QScheduleSpace ss = QScheduleSpace.scheduleSpace;
    QScheduleSpaceMember ssm = QScheduleSpaceMember.scheduleSpaceMember;

    return jpaQueryFactory
      .select(Projections.constructor(
        PersonaSpaceResponse.class,
        ss.id,
        ss.name,
        ss.description,
        ss.spaceColor
      ))
      .from(ss)
      .join(ssm).on(ss.id.eq(ssm.scheduleSpaceId))
      .where(
        ssm.memberId.eq(memberId),
        ssm.status.eq(SpaceMemberStatus.ACTIVE),
        ss.spaceType.eq(spaceType),
        ss.status.eq(SpaceStatus.ACTIVE)
      )
      .orderBy(ss.createdAt.asc())
      .fetch();
  }

}
