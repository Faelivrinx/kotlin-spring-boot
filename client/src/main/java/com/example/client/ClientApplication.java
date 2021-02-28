package com.example.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}

@Slf4j
@RestController
class Controller {

    @GetMapping
    Mono<UserId> getUserId(){
        return Mono.fromCallable(() -> new UserId(UUID.randomUUID()));

    }

    @Data
    static class UserId {
        private final UUID userId;
    }
}
