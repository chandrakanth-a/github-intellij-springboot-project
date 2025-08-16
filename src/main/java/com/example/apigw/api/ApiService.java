package com.example.apigw.api;

import com.example.apigw.api.dto.FetchResultResponse;
import com.example.apigw.api.dto.SubmitUrlRequest;
import com.example.apigw.api.dto.UrlResponse;
import com.example.apigw.strategy.ExternalApiResult;
import com.example.apigw.strategy.ExternalApiStrategy;
import com.example.apigw.strategy.StrategyFactory;
import com.example.apigw.user.User;
import com.example.apigw.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Service
public class ApiService {

    private final ApiEndpointRepository endpointRepository;
    private final ApiFetchResultRepository resultRepository;
    private final UserRepository userRepository;
    private final StrategyFactory strategyFactory;

    public ApiService(ApiEndpointRepository endpointRepository,
                      ApiFetchResultRepository resultRepository,
                      UserRepository userRepository,
                      StrategyFactory strategyFactory) {
        this.endpointRepository = endpointRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.strategyFactory = strategyFactory;
    }

    private void validateUrl(String url) {
        if (!StringUtils.hasText(url)) throw new IllegalArgumentException("URL is required");
        try {
            URI u = new URI(url);
            if (u.getScheme() == null || u.getHost() == null) throw new IllegalArgumentException("URL must include scheme and host");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    @Transactional
    public UrlResponse addUrl(User owner, SubmitUrlRequest request) {
        validateUrl(request.url());
        ApiEndpoint e = new ApiEndpoint();
        e.setOwner(owner);
        e.setUrl(request.url());
        e.setName(request.name());
        e = endpointRepository.save(e);
        return new UrlResponse(e.getId(), e.getUrl(), e.getName(), e.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<UrlResponse> listUrls(User owner) {
        return endpointRepository.findByOwnerIdOrderByCreatedAtDesc(owner.getId())
                .stream().map(e -> new UrlResponse(e.getId(), e.getUrl(), e.getName(), e.getCreatedAt()))
                .toList();
    }

    @Transactional
    public FetchResultResponse fetchNow(User owner, UUID endpointId) {
        ApiEndpoint endpoint = endpointRepository.findByIdAndOwnerId(endpointId, owner.getId())
                .orElseThrow(() -> new RuntimeException("Endpoint not found or not yours"));
        ExternalApiStrategy strategy = strategyFactory.resolve(endpoint.getUrl());
        ExternalApiResult res = strategy.fetch(endpoint.getUrl());

        ApiFetchResult r = new ApiFetchResult();
        r.setEndpoint(endpoint);
        r.setOwner(owner);
        r.setSuccess(res.success());
        r.setStatusCode(res.statusCode());
        r.setContentType(res.contentType());
        r.setResponseBody(res.body());
        r.setDurationMs(res.durationMs());
        r = resultRepository.save(r);

        String preview = res.body() == null ? "" : res.body().substring(0, Math.min(500, res.body().length()));
        return new FetchResultResponse(r.getId(), endpoint.getId(), r.isSuccess(), r.getStatusCode(),
                r.getContentType(), r.getDurationMs(), r.getFetchedAt(), preview);
    }

    @Transactional(readOnly = true)
    public List<FetchResultResponse> listResults(User owner, UUID endpointId) {
        var results = (endpointId == null)
                ? resultRepository.findByOwnerIdOrderByFetchedAtDesc(owner.getId())
                : resultRepository.findByOwnerIdAndEndpointIdOrderByFetchedAtDesc(owner.getId(), endpointId);
        return results.stream().map(r -> new FetchResultResponse(
                r.getId(),
                r.getEndpoint().getId(),
                r.isSuccess(),
                r.getStatusCode(),
                r.getContentType(),
                r.getDurationMs(),
                r.getFetchedAt(),
                r.getResponseBody() == null ? "" : r.getResponseBody().substring(0, Math.min(500, r.getResponseBody().length()))
        )).toList();
    }

    @Transactional(readOnly = true)
    public FetchResultResponse getResult(User owner, UUID id) {
        var r = resultRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Result not found or not yours"));
        return new FetchResultResponse(
                r.getId(), r.getEndpoint().getId(), r.isSuccess(), r.getStatusCode(),
                r.getContentType(), r.getDurationMs(), r.getFetchedAt(),
                r.getResponseBody() == null ? "" : r.getResponseBody().substring(0, Math.min(10000, r.getResponseBody().length()))
        );
    }
}
