package com.example.manager

import com.example.manager.client.ReportsAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Instant
import java.util.*
import kotlin.random.Random

@SpringBootApplication
class ManagerApplication

fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}

@RestController
@RequestMapping(value = ["/tasks/1"])
class MainController(val client: ReportsAdapter) {

    val logger = logger<MainController>()

    @GetMapping
    fun getAllTasks(): Mono<List<Task>> {
        return Flux.range(0, Random.nextInt(10))
                .map { Task("Task ${UUID.randomUUID()}", Instant.now()) }
                .collectList()
    }

    @PostMapping
    fun createTask(@RequestBody body: CreateTaskRequest): Mono<ResponseEntity<Void>> {
        val dummyCreatedTask = Task("Task ${body.task} with id: ${UUID.randomUUID()}", Instant.now())
        return client.createReport(dummyCreatedTask)
                .map { ResponseEntity.created(URI.create(it.taskId.toString())).build<Void>() }
    }

    data class CreateTaskRequest(val task: String) {

    }
}
