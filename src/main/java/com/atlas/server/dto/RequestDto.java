package com.atlas.server.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    private String corporateId;
    private String requestType;
    private String payload;
    private LocalDateTime createdAt;
}