package com.example.bookmanagementms.service;

import com.example.bookmanagementms.util.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.List;

import static com.example.bookmanagementms.util.HttpConstants.AUTH_HEADER;
import static com.example.bookmanagementms.util.HttpConstants.BEARER_AUTH_HEADER;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    String secret;
    private final SecurityProperties securityProperties;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String getSubject(){
        String token = getToken();
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    public boolean isAuthor(){
        String token = getToken();
        Claims claims = getClaims(token);
        List<?> roles = claims.get("role", List.class);

        return roles.contains("AUTHOR");
    }

    public String getToken(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(AUTH_HEADER).substring(BEARER_AUTH_HEADER.length()).trim();
        return token;
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
