package com.example.manager

import com.example.manager.client.OutgoingRestClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@SpringBootApplication
class ManagerApplication

fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}

@RestController
class MainController(val client: OutgoingRestClient) {

    val logger = logger<MainController>()

    @GetMapping
    fun request() : Mono<OutgoingRestClient.UserId> {
        return client.makeRequest()
    }
}
