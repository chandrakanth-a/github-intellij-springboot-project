package com.example.apigw.api;

import com.example.apigw.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_fetch_results")
@Getter @Setter @NoArgsConstructor
public class ApiFetchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id")
    private ApiEndpoint endpoint;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    private Integer statusCode;
    private boolean success;

    @Column(length = 255)
    private String contentType;

    @Column(columnDefinition = "text")
    private String responseBody;

    private Long durationMs;

    @Column(nullable = false)
    private Instant fetchedAt = Instant.now();
}
