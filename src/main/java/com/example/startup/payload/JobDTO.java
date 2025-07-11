package com.example.startup.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JobDTO(
        UUID id,
        String title,
        String description,
        String workerName,
        String workerPhone,
        String jobType,
        String jobStatus,
        Double latitude,
        Double longitude,
        List<String> imgUrls
) {
}
