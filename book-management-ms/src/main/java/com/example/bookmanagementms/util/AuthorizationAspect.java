package com.example.bookmanagementms.util;

import com.example.bookmanagementms.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.security.Key;
import java.util.List;

@Slf4j
@Aspect
@Component
public class AuthorizationAspect {
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("SGVsbG8gbXkgbmFtZSBpcyBLYW5hbi4gVGhpcyBpcyBteSBzZWN1cml0eSBzZWNyZXQuIElmIHlvdSBjYW4gZW5jeXJwdGUgaXQsIGkgZG9uJ3QgaGF2ZSBhbnkgd29yZC4gVGVhY2ggbWUsIHdlIGJyZWFrIGV2ZXJ5b25lJ3MgdG9nZXRoZXI="));

    @Before("@annotation(com.example.bookmanagementms.util.HasRoleAuthor)")
    public void authorize() throws ValidationException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization").substring("Bearer".length()).trim();

        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<?> roles = claims.get("rule", List.class);
        log.info("roles: {}", roles);
        log.info("result: {}", roles.contains("AUTHOR"));

        if (!roles.contains("AUTHOR")) {
            throw new CustomException("User does not have the required role to perform this action.");
        }
    }
}
