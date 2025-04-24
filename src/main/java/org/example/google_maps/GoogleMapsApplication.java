package org.example.google_maps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example")
public class GoogleMapsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleMapsApplication.class, args);
    }

}
