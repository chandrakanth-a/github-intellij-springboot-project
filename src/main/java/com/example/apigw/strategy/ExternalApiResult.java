package com.example.apigw.strategy;

public record ExternalApiResult(
        boolean success,
        int statusCode,
        String contentType,
        String body,
        long durationMs
) {}
