package com.app.daymoya.domain.common.code.repository;

import com.app.daymoya.domain.common.code.entity.Code;
import com.app.daymoya.domain.common.code.entity.CodeUseYn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {

  List<Code> findByGrpCodeIdAndUseYnOrderBySortOrderAsc(String grpCodeId, CodeUseYn useYn);

  Optional<Code> findByGrpCodeIdAndCodeAndUseYn(String grpCodeId, String code, CodeUseYn codeUseYn);

}
