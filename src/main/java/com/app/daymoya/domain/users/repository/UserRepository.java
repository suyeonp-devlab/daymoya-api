package com.app.daymoya.domain.users.repository;

import com.app.daymoya.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  boolean existsByEmailAndDeletedAtIsNull(String email);

}
