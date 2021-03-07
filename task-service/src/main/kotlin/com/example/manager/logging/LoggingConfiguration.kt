package com.example.manager.logging

import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter

@Configuration
class LoggingConfiguration {

    @Bean
    fun loggingWebFilter () : WebFilter {
        return LoggingFilter()
    }
}