package com.example.client.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    LoggingFilter loggingFilter(){
        return new LoggingFilter();
    }

}
