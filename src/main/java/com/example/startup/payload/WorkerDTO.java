package com.example.startup.payload;

import com.example.startup.entity.enums.UserStatus;

import java.util.UUID;

public record WorkerDTO(
        UUID id,
        String name,
        String phone,
        boolean IMBusy,
        String key,
        UserStatus status
) {
}
