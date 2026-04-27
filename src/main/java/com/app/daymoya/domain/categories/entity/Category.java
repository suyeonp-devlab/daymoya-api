package com.app.daymoya.domain.categories.entity;

import com.app.daymoya.global.entity.BaseTimeEntity;
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
public class Category extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 카테고리 이름
  @Column(nullable = false, length = 50)
  private String name;

  // 카테고리 범위
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CategoryScopeType scopeType;

  // 사용자 ID
  private Long scopeUserId;

  // 그룹 ID
  private Long scopeGroupId;

  // 카테고리 색상
  @Column(length = 7)
  private String color;

  // 카테고리 표시 순서
  @Column(nullable = false)
  private Integer displayOrder;

  /** 개인 카테고리 생성 */
  public static Category createPersonal(
     String name
    ,Long scopeUserId
    ,String color
    ,Integer displayOrder
  ) {

    return Category.builder()
      .name(name)
      .scopeType(CategoryScopeType.PERSONAL)
      .scopeUserId(scopeUserId)
      .color(color)
      .displayOrder(displayOrder)
      .build();
  }

  /** 카테고리 수정 */
  public void update(String name, String color) {
    this.name = name;
    this.color = color;
  }

  /** 카테고리 순서 변경 */
  public void updateDisplayOrder(int displayOrder) {
    this.displayOrder = displayOrder;
  }

}
