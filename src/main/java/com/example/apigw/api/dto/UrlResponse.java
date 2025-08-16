package com.example.apigw.api.dto;

import java.time.Instant;
import java.util.UUID;

public record UrlResponse(
        UUID id,
        String url,
        String name,
        Instant createdAt
) {}
