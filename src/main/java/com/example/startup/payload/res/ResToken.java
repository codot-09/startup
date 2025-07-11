package com.example.startup.payload.res;

import lombok.Builder;

@Builder
public record ResToken (
        String accessToken,
        String refreshToken,
        String type,
        String role
){
}
