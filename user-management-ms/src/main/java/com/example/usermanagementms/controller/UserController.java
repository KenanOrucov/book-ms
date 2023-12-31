package com.example.usermanagementms.controller;

import com.example.usermanagementms.domain.Role;
import com.example.usermanagementms.dto.request.SignInRequest;
import com.example.usermanagementms.dto.request.UserRequestDto;
import com.example.usermanagementms.dto.response.SignInResponse;
import com.example.usermanagementms.dto.response.SignUpResponse;
import com.example.usermanagementms.dto.response.UserResponseDto;
import com.example.usermanagementms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;


    @PostMapping("/register/user")
    public SignUpResponse registerUser(
            @RequestBody UserRequestDto request
    ) {
        return userService.saveUser(request, Role.USER);
    }

    @PostMapping("/register/author")
    public SignUpResponse registerAuthor(
            @RequestBody UserRequestDto request
    ) {
        return userService.saveUser(request, Role.AUTHOR);
    }

    @PostMapping("/login/user")
    public ResponseEntity<SignInResponse> loginUser(
            @RequestBody SignInRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request, Role.USER));
    }

    @PostMapping("/login/author")
    public ResponseEntity<SignInResponse> loginAuthor(
            @RequestBody SignInRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request, Role.AUTHOR));
    }

    @GetMapping("/api")
    private List<UserResponseDto> getAll(){
        return userService.getAll();
    }

    @GetMapping("/api/{id}")
    private UserResponseDto getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @DeleteMapping("/api/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
