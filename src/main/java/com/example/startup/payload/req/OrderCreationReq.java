package com.example.startup.payload.req;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record OrderCreationReq(

        @NotNull(message = "Latitude must not be null")
        @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
        @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
        Double clientLatitude,

        @NotNull(message = "Longitude must not be null")
        @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
        Double clientLongitude,

        @NotNull(message = "Order date must not be null")
        @FutureOrPresent(message = "Order date must be today or in the future")
        LocalDate orderDate

) {}
