package com.example.usermanagementms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Integer age;
}