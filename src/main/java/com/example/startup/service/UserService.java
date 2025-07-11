package com.example.startup.service;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.entity.enums.UserStatus;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.UserDTO;
import com.example.startup.payload.WorkerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    boolean checkUserExistsByPhone(String phone);
    String createUserAndReturnKey(String phone,String role);

    ApiResponse<UserDTO> showProfile(User currentUser);
    ApiResponse<String> updateName(User currentUser,String newName);

    ApiResponse<List<UserDTO>> findUsers(UserStatus status,UserRole role, String field);

    ApiResponse<String> checkIMBusy(User currentUser,boolean status);

    ApiResponse<UserDTO> findById(UUID id);
}
