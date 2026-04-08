package com.ocsentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableScheduling
public class OcSentinelApplication {
    
    private static final Logger log = LoggerFactory.getLogger(OcSentinelApplication.class);

    public static void main(String[] args) {
        log.info("=== OC Sentinel V2 Starting ===");
        log.info("Java Version: {}", System.getProperty("java.version"));
        log.info("Spring Boot Version: {}", SpringBootApplication.class.getPackage().getImplementationVersion());
        
        try {
            var app = new SpringApplication(OcSentinelApplication.class);
            var env = app.run(args).getEnvironment();
            
            log.info("=== Application Started Successfully ===");
            log.info("Server Port: {}", env.getProperty("server.port", "8080"));
            log.info("Active Profiles: {}", String.join(", ", env.getActiveProfiles()));
            log.info("Application Name: {}", env.getProperty("spring.application.name"));
            log.info("=====================================");
            
        } catch (Exception e) {
            log.error("=== APPLICATION STARTUP FAILED ===", e);
            System.exit(1);
        }
    }
}
