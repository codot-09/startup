package com.example.startup.service.impl;

import com.example.startup.entity.User;
import com.example.startup.exception.RestException; // RestException ni import qilamiz
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.LoginByKeyDto;
import com.example.startup.payload.res.ResToken;
import com.example.startup.repository.UserRepository;
import com.example.startup.security.JwtProvider;
import com.example.startup.service.AuthService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // HttpStatus ni ham import qilamiz
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public ApiResponse<ResToken> login(LoginByKeyDto loginDto) {
        User user = userRepository.findByKey(loginDto.key())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String accessToken = jwtProvider.generateToken(user.getKey());
        String refreshToken = jwtProvider.generateRefreshToken(user.getKey());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return ApiResponse.ok(
                "Login successfully",
                ResToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .type("Bearer")
                        .role(user.getRole().name())
                        .build()
        );
    }

    @Override
    public ApiResponse<String> refreshToken(String refreshToken) {
        if (jwtProvider.isRefreshToken(refreshToken)){
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            return ApiResponse.ok(null,jwtProvider.generateToken(user.getKey()));
        }
        return ApiResponse.error("This not refresh token",null);
    }
}