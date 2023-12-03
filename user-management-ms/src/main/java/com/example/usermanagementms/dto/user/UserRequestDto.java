package com.example.usermanagementms.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDto {
    String firstName;
    String lastName;
    String email;
    String password;
    LocalDate birthDate;
}
