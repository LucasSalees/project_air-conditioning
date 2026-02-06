package com.system_air.project_air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.system_air.project_airconditioning.model") // Ajuste para o seu pacote de model
@EnableJpaRepositories("com.system_air.project_airconditioning.repository") // Ajuste para o seu pacote de repo
public class ProjectAirConditioningApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectAirConditioningApplication.class, args);
    }
}
