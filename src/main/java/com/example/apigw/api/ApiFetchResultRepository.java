package com.example.apigw.api;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiFetchResultRepository extends JpaRepository<ApiFetchResult, UUID> {
    List<ApiFetchResult> findByOwnerIdOrderByFetchedAtDesc(UUID ownerId);
    List<ApiFetchResult> findByOwnerIdAndEndpointIdOrderByFetchedAtDesc(UUID ownerId, UUID endpointId);
    Optional<ApiFetchResult> findByIdAndOwnerId(UUID id, UUID ownerId);
}
