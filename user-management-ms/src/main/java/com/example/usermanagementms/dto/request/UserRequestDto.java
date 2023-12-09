package com.example.usermanagementms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Integer age;
}