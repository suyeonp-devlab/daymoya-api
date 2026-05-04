package com.app.daymoya.domain.codes.repository;

import com.app.daymoya.domain.codes.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {

}
