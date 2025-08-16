package com.example.apigw.api;

import com.example.apigw.api.dto.FetchResultResponse;
import com.example.apigw.api.dto.SubmitUrlRequest;
import com.example.apigw.api.dto.UrlResponse;
import com.example.apigw.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/urls")
    public ResponseEntity<UrlResponse> addUrl(@AuthenticationPrincipal User user,
                                              @Valid @RequestBody SubmitUrlRequest request) {
        return ResponseEntity.ok(apiService.addUrl(user, request));
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlResponse>> listUrls(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(apiService.listUrls(user));
    }

    @PostMapping("/urls/{id}/fetch")
    public ResponseEntity<FetchResultResponse> fetchNow(@AuthenticationPrincipal User user,
                                                        @PathVariable("id") UUID id) {
        return ResponseEntity.ok(apiService.fetchNow(user, id));
    }

    @GetMapping("/dashboard/results")
    public ResponseEntity<List<FetchResultResponse>> listResults(@AuthenticationPrincipal User user,
                                                                 @RequestParam(value = "endpointId", required = false) UUID endpointId) {
        return ResponseEntity.ok(apiService.listResults(user, endpointId));
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<FetchResultResponse> getResult(@AuthenticationPrincipal User user,
                                                         @PathVariable("id") UUID id) {
        return ResponseEntity.ok(apiService.getResult(user, id));
    }
}
