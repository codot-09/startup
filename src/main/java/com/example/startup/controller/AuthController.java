package com.example.startup.controller;

import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.LoginByKeyDto;
import com.example.startup.payload.res.ResToken;
import com.example.startup.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Autentfikatsiya api",description = "login va refresh token uchun api endpointlar")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Login", description = "Token,token turi qaytadi")
    public ResponseEntity<ApiResponse<ResToken>> login(
            @RequestBody LoginByKeyDto key
    ) {
        return ResponseEntity.ok(authService.login(key));
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "Yangi access token olish uchun")
    public ResponseEntity<ApiResponse<String>> refreshToken(
            @RequestParam String refreshToken
    ){
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
