package com.app.daymoya;

import org.springframework.boot.SpringApplication;

public class TestDaymoyaApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(DaymoyaApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
