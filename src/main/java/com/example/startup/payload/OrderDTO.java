package com.example.startup.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
        UUID id,
        LocalDate orderDate,
        UUID workerId,
        Double latitude,
        Double longitude,
        String jobType,
        UUID jobId
) {
}
