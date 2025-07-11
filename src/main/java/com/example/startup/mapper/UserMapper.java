package com.example.startup.mapper;

import com.example.startup.entity.User;
import com.example.startup.payload.UserDTO;
import com.example.startup.payload.WorkerDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserMapper {

    public UserDTO toDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .name(user.getName())
                .key(user.getKey())
                .iMBusy(user.isIMBusy())
                .build();
    }

    public List<UserDTO> toDTOList(List<User> users){
        return users.stream()
                .map(this::toDTO)
                .toList();
    }
}
