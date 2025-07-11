package com.example.startup.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record FeedbackDTO (
        @Schema(hidden = true)
        UUID id,
        String message,
        int rating
){
}
