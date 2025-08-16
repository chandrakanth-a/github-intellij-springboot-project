package com.example.apigw.strategy;

public interface ExternalApiStrategy {
    boolean supports(String url);
    ExternalApiResult fetch(String url);
}
