package com.example.startup.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        UUID id,
        String phone,
        String key,
        String name,
        boolean iMBusy
) {
}
