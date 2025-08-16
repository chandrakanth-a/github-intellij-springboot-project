package com.example.apigw.user.dto;

public record LoginResponse(
        String token,
        long expiresInMillis
) {}
