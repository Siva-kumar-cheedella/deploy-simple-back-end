package com.cleanlearn.integration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GroqClient {
    
    @Value("${groq.api.key}")
    private String gorqApiKey;

    @Bean
    public RestClient groqRestClient() {

        return RestClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader("Authorization", "Bearer " + gorqApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}