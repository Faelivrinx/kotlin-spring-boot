package com.example.client;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskReportResponse {
    private final UUID taskId;
    private final String description;
}
