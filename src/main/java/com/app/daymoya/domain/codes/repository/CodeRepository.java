package com.app.daymoya.domain.codes.repository;

import com.app.daymoya.domain.codes.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Long> {

  List<Code> findAllByGroupIdOrderBySortNoAsc(Long groupId);

  boolean existsByGroupIdAndCode(Long groupId, String code);

}
