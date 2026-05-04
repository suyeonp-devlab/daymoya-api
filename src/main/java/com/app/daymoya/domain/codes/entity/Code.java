package com.app.daymoya.domain.codes.entity;

import com.app.daymoya.global.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "codes")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Code extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 그룹코드 ID
  @Column(nullable = false)
  private Long groupId;

  // 코드
  @Column(nullable = false, length = 20)
  private String code;

  // 코드명
  @Column(nullable = false, length = 50)
  private String codeName;

  // 코드설명
  private String description;

  // 기타1
  private String etc1;

  // 기타2
  private String etc2;

  // 기타3
  private String etc3;

  // 사용여부
  @Column(nullable = false)
  private Boolean enabled;

  // 정렬순서
  @Column(nullable = false)
  private Integer sortNo;

}
