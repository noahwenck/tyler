package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TylerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TylerApplication.class, args);
    }

    /**
     * Used for HTTP requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Used for parsing Json into objects
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}