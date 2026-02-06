package com.system_air.project_air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.system_air"}) // Isso resolve o conflito dos nomes dos pacotes
public class ProjectAirConditioningApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectAirConditioningApplication.class, args);
    }
}