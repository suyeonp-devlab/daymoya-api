package com.app.daymoya.domain.tasks.repository;

import com.app.daymoya.domain.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
