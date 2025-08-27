package com.atlas.server.repository;

import com.atlas.server.model.OutboxRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRequestRepository extends JpaRepository<OutboxRequest, Long> {
    List<OutboxRequest> findAllByStatus(OutboxRequest.Status status);
    Optional<OutboxRequest> findByCorporateId(String corporateId);
}