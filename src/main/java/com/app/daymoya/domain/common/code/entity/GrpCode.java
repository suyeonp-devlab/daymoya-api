package com.app.daymoya.domain.common.code.entity;

import com.app.daymoya.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "grp_codes")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GrpCode extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 그룹코드 ID
  @Column(nullable = false, length = 20)
  private String grpCodeId;

  // 그룹코드 이름
  @Column(nullable = false, length = 100)
  private String grpCodeName;

  // 설명
  private String description;

  // 사용 여부 (Y/N)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 1)
  private CodeUseYn useYn;

}
