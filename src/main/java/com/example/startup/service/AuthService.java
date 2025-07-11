package com.example.startup.service;

import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.LoginByKeyDto;
import com.example.startup.payload.res.ResToken;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse<ResToken> login(LoginByKeyDto dto);

    ApiResponse<String> refreshToken(String refreshToken);
}
