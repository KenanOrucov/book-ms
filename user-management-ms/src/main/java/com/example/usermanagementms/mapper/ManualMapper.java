package com.example.usermanagementms.mapper;

import com.example.usermanagementms.domain.User;
import com.example.usermanagementms.dto.response.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ManualMapper {
    public UserResponseDto convertToUserResponseDto(User user){
        return UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .age(user.getAge())
                .build();
    }

    public List<UserResponseDto> convertToUserResponseDtos(List<User> users){
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user: users){
            userResponseDtos.add(convertToUserResponseDto(user));
        }
        return userResponseDtos;
    }
}
