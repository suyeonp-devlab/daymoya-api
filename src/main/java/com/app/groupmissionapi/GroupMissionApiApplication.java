package com.app.groupmissionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GroupMissionApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(GroupMissionApiApplication.class, args);
  }

}
