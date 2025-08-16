package com.example.apigw.api.dto;

import java.time.Instant;
import java.util.UUID;

public record FetchResultResponse(
        UUID id,
        UUID endpointId,
        boolean success,
        Integer statusCode,
        String contentType,
        Long durationMs,
        Instant fetchedAt,
        String responseBodyPreview
) {}
