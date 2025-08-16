package com.example.apigw.strategy;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Component
public class GenericJsonStrategy implements ExternalApiStrategy {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supports(String url) {
        // Fallback for any URL (prefer more specific strategies before this)
        return true;
    }

    @Override
    public ExternalApiResult fetch(String url) {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            sw.stop();
            String contentType = resp.getHeaders().getContentType() != null
                    ? resp.getHeaders().getContentType().toString()
                    : "application/json";
            return new ExternalApiResult(true, resp.getStatusCode().value(), contentType, resp.getBody(), sw.getTotalTimeMillis());
        } catch (Exception ex) {
            sw.stop();
            String message = ex.getMessage() != null ? ex.getMessage() : "Request failed";
            return new ExternalApiResult(false, 0, "text/plain", message, sw.getTotalTimeMillis());
        }
    }
}
