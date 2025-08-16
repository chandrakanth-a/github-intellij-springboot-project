package com.example.apigw.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmitUrlRequest(
        @NotBlank @Size(max = 4096) String url,
        @Size(max = 255) String name
) {}
