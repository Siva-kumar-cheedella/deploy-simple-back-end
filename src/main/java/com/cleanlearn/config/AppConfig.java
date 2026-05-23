package com.cleanlearn.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@Data
public class AppConfig {
    
    @Value("${token.validation.enabled}")
    private boolean tokenValidationEnabled;
}