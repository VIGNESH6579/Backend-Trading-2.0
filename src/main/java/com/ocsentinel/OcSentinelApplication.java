package com.ocsentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OcSentinelApplication {
    public static void main(String[] args) {
        SpringApplication.run(OcSentinelApplication.class, args);
    }
}
