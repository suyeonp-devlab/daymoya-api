package com.app.daymoya.domain.users.repository;

import com.app.daymoya.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
