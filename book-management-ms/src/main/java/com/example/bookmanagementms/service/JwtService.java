package com.example.bookmanagementms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.List;

@Service
public class JwtService {
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("SGVsbG8gbXkgbmFtZSBpcyBLYW5hbi4gVGhpcyBpcyBteSBzZWN1cml0eSBzZWNyZXQuIElmIHlvdSBjYW4gZW5jeXJwdGUgaXQsIGkgZG9uJ3QgaGF2ZSBhbnkgd29yZC4gVGVhY2ggbWUsIHdlIGJyZWFrIGV2ZXJ5b25lJ3MgdG9nZXRoZXI="));

    public String getSubject(){
        String token = getToken();
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    public boolean isAuthor(){
        String token = getToken();
        Claims claims = getClaims(token);
        List<?> roles = claims.get("rule", List.class);

        return roles.contains("AUTHOR");
    }

    public String getToken(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization").substring("Bearer".length()).trim();
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
