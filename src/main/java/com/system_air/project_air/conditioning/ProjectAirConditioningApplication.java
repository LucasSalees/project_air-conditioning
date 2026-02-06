package com.system_air.project_air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.system_air")
@EntityScan(basePackages = "com.system_air")
@EnableJpaRepositories(basePackages = "com.system_air")
public class ProjectAirConditioningApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectAirConditioningApplication.class, args);
    }
}