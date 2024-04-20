package com.example.santa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SantaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SantaApplication.class, args);
    }

}
