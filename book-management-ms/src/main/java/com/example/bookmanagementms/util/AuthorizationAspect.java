package com.example.bookmanagementms.util;

import com.example.bookmanagementms.exception.CustomException;
import com.example.bookmanagementms.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.modelmapper.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
    private final JwtService jwtService;

    @Before("@annotation(com.example.bookmanagementms.util.HasRoleAuthor)")
    public void authorize() throws ValidationException {
        String token = jwtService.getToken();
        var claims = jwtService.getClaims(token);

        List<?> roles = claims.get("role", List.class);
        log.info("roles: {}", roles);
        log.info("result: {}", roles.contains("AUTHOR"));

        if (!jwtService.isAuthor()) {
            throw new CustomException("User does not have the required role to perform this action.");
        }
    }
}
