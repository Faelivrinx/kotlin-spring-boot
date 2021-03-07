package com.example.manager.client

import com.example.manager.Task
import com.example.manager.logger
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.*
import java.util.*

@Service
class ReportsAdapter(val client: WebClient) {

    val logger = logger<ReportsAdapter>()

    fun createReport(task: Task): Mono<Report> {
        return client
                .post()
                .bodyValue(task)
                .exchangeToMono {
                    it.bodyToMono<Report>()
                }
    }

    data class Report(val taskId: UUID, val description: String)
}