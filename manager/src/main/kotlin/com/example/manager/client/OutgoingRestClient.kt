package com.example.manager.client

import org.slf4j.MDC
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.util.*

@Service
class OutgoingRestClient {

    val client: WebClient = WebClient.builder()
            .baseUrl("http://client-service:9090/")
            .build()

    fun makeRequest() : Mono<UserId> {
        return client
                .get()
                .header("X-B3-TRACEID", MDC.get("X-B3-TraceId"))
                .exchangeToMono {
            it.bodyToMono<UserId>()
        }
    }

    data class UserId (val userId: UUID)
}