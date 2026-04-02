package com.app.daymoya.domain.common.code.repository;

import com.app.daymoya.domain.common.code.entity.CodeUseYn;
import com.app.daymoya.domain.common.code.entity.GrpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrpCodeRepository extends JpaRepository<GrpCode, Long> {

  Optional<GrpCode> findByGrpCodeIdAndUseYn(String grpCodeId, CodeUseYn useYn);

}
