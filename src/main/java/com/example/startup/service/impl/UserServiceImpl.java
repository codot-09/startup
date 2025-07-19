package com.example.startup.service.impl;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.entity.enums.UserStatus;
import com.example.startup.exception.ResourceNotFoundException;
import com.example.startup.exception.RestException;
import com.example.startup.mapper.UserMapper;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.UserDTO;
import com.example.startup.repository.UserRepository;
import com.example.startup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    @Override
    public boolean checkUserExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public String createUserAndReturnKey(String phone,String role,Long chatId) {
        String key = generateRandomCode();

        User newUser = User.builder()
                .phone(phone)
                .chatId(chatId)
                .key(key)
                .name("User")
                .role(UserRole.valueOf(role))
                .status(UserStatus.WAITING).build();
        userRepository.save(newUser);
        return key;
    }

    @Override
    public ApiResponse<UserDTO> showProfile(User currentUser) {
        return ApiResponse.ok(mapper.toDTO(currentUser));
    }

    @Override
    public ApiResponse<String> updateName(User currentUser,String newName) {
        currentUser.setName(newName);

        userRepository.save(currentUser);

        return ApiResponse.ok("Ism o'zgartirildi",null);
    }

    @Override
    public ApiResponse<List<UserDTO>> findUsers(UserStatus status,UserRole role,String field) {
        String roleName = role != null ? role.name() : null;
        String statusName = status != null ? status.name() : null;
        List<User> foundUsers = userRepository.findUsers(roleName,field,statusName);
        List<UserDTO> users;

        if (!foundUsers.isEmpty()){
            users = mapper.toDTOList(foundUsers);
            return ApiResponse.ok(users);
        }

        return ApiResponse.ok((List<UserDTO>) null);
    }

    @Override
    public ApiResponse<String> checkIMBusy(User currentUser, boolean status) {
        currentUser.setIMBusy(status);
        userRepository.save(currentUser);
        return ApiResponse.ok("Bandlik: " + status,null);
    }

    @Override
    public ApiResponse<UserDTO> findById(UUID id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi"));

        return ApiResponse.ok(mapper.toDTO(foundUser));
    }

    public static String generateRandomCode() {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    
}
