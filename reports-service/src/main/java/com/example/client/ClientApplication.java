package com.example.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;
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

    @PostMapping
    Mono<TaskReportResponse> createReport(@RequestBody TaskReportRequest request){
        return Mono.fromCallable(() -> new TaskReportResponse(UUID.randomUUID(), "Dummy report of task:" + request.getTaskName()))
                .delayElement(Duration.ofSeconds(2));
    }

}
