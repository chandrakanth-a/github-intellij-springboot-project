package com.example.apigw.api;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, UUID> {
    List<ApiEndpoint> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);
    Optional<ApiEndpoint> findByIdAndOwnerId(UUID id, UUID ownerId);
}
