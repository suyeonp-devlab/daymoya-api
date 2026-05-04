package com.app.daymoya.domain.categories.entity;

import com.app.daymoya.global.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Category extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 카테고리명
  @Column(nullable = false, length = 10)
  private String name;

  // 범위(공통코드 CATEGORY-001)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CategoryScope scope;

  // 사용자 ID
  private Long scopeUserId;

  // 그룹 ID
  private Long scopeGroupId;

  // 색상 (HEX 6자리 형식, 예: #FFFFFF)
  @Column(length = 7)
  private String color;

  // 정렬순서
  @Column(nullable = false)
  private Integer sortNo;

}
