package org.example.google_maps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.example")
@EnableJpaRepositories(basePackages = "org.example")
@EntityScan(basePackages = "org.example.entity")
public class GoogleMapsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleMapsApplication.class, args);
    }

}
