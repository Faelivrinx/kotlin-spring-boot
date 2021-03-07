package com.example.client;

import lombok.Data;

import java.time.Instant;

@Data
public class TaskReportRequest {
    private final String taskName;
    private final Instant createdAt;
}
