package com.atlas.server.repository;

import com.atlas.server.model.InboxRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxRequestRepository extends JpaRepository<InboxRequest, Long> {
    List<InboxRequest> findAllByStatus(InboxRequest.Status status);
}