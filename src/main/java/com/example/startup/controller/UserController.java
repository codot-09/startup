package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.entity.enums.UserStatus;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.UserDTO;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Foydalanuvchi api",description = "Oddiy foydalanuvchilar va ularni boshqarish uchun api endpointlar")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "O'z profilini ko'rish")
    public ResponseEntity<ApiResponse<UserDTO>> showProfile(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(userService.showProfile(user));
    }

    @PatchMapping
    @Operation(summary = "Ism o'zgartirish")
    public ResponseEntity<ApiResponse<String>> updateName(
            @CurrentUser User user,
            @RequestParam String name
    ){
        return ResponseEntity.ok(userService.updateName(user, name));
    }

    @GetMapping()
    @Operation(summary = "ADMIN foydalanuvchilarni qidirish")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findUsers(
            @RequestParam(required = false) String field,
            @RequestParam(required = false)UserRole role,
            @RequestParam(required = false)UserStatus status
    ){
        return ResponseEntity.ok(userService.findUsers(status, role, field));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getById(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/busy")
    public ResponseEntity<ApiResponse<String>> checkBusy(
            @CurrentUser User user,
            @RequestParam boolean status
    ){
        return ResponseEntity.ok(userService.checkIMBusy(user, status));
    }
}
