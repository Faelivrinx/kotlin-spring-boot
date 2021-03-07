package com.example.manager.client

import com.example.manager.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class BasicWebClient {

    val logger = logger<BasicWebClient>()

    @Bean
    fun clientWebClient(): WebClient {
        return WebClient.builder()
//                .filter(ExchangeFilterFunction { clientRequest, next ->
//                    logger.info("Request " + clientRequest.method().toString())
//                    return@ExchangeFilterFunction next.exchange(clientRequest)
//                })
                .baseUrl("http://reports-service:9090/")
//                .baseUrl("http://localhost:9090/")
                .build()
    }
}