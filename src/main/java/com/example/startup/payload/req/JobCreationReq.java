package com.example.startup.payload.req;

import jakarta.validation.constraints.*;

public record JobCreationReq(

        @NotBlank(message = "Title must not be blank")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        String title,

        @NotBlank(message = "Description must not be blank")
        @Size(min = 10, message = "Description must be at least 10 characters long")
        String description,

        @NotNull(message = "Latitude must not be null")
        @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
        @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
        Double latitude,

        @NotNull(message = "Longitude must not be null")
        @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
        Double longitude

) {}
