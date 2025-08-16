package com.example.apigw.strategy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StrategyFactory {

    private final List<ExternalApiStrategy> strategies;

    public StrategyFactory(List<ExternalApiStrategy> strategies) {
        this.strategies = strategies;
    }

    public ExternalApiStrategy resolve(String url) {
        return strategies.stream()
                .filter(s -> s.supports(url))
                .findFirst()
                .orElseGet(() -> strategies.stream()
                        .filter(s -> s.getClass().getSimpleName().equals("GenericJsonStrategy"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No suitable strategy found")));
    }
}
