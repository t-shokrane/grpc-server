package com.atlas.server.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inbox_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "corporate_id", nullable = false, unique = true)
    private String corporateId;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "retry_count")
    private int retryCount = 0;

    @Column(name = "timeout_at")
    private LocalDateTime timeoutAt;

    public enum Status { PENDING, SENT, FAILED }
}
