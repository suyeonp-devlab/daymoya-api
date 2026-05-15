package com.app.daymoya.domain.codes.entity;

import com.app.daymoya.global.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "code_groups")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CodeGroup extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 그룹코드
  @Column(nullable = false, unique = true, length = 20)
  private String groupCode;

  // 그룹명
  @Column(nullable = false, length = 50)
  private String groupName;

  // 그룹설명
  private String description;

  // 업무(공통코드 CMN-001)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CodeGroupDomain domain;

  // 사용여부
  @Column(nullable = false)
  private Boolean enabled;

  /** 그룹코드 생성 */
  public static CodeGroup create(
    String groupCode,
    String groupName,
    String description,
    CodeGroupDomain domain,
    Boolean enabled
  ) {

    return CodeGroup.builder()
      .groupCode(groupCode)
      .groupName(groupName)
      .description(description)
      .domain(domain)
      .enabled(enabled)
      .build();
  }

  /** 그룹코드 변경 */
  public void update(
    String groupName,
    String description,
    CodeGroupDomain domain,
    Boolean enabled
  ) {
    this.groupName = groupName;
    this.description = description;
    this.domain = domain;
    this.enabled = enabled;
  }

}
