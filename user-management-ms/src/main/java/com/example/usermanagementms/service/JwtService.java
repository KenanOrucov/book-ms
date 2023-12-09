package com.example.usermanagementms.service;

import com.example.usermanagementms.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE)
public class JwtService {
    @Value("${jwt.secret}")
    String secret;
    @Value("${jwt.access-token.expiration}")
    long accessTokenExpiration;
    @Value("${jwt.refresh-token.expiration}")
    long refreshTokenExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, Role role) {
        return generateToken(new HashMap<>(), userDetails, role);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Role role
    ) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration, role);
    }

    public String generateRefreshToken(
            UserDetails userDetails, Role role
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration, role);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration,
            Role role
    ) {
        final JwtBuilder jwtBuilder =  Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);
        addclaimsSet(jwtBuilder, userDetails, role);
        return jwtBuilder.compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void addclaimsSet(JwtBuilder jwtBuilder, UserDetails authenticationToken, Role role) {
        Collection<? extends GrantedAuthority> authorities = authenticationToken.getAuthorities();
        List rules = authorities.stream().map(a-> a.getAuthority()).collect(Collectors.toList());
        log.trace("role: {}", authorities);
        jwtBuilder.addClaims(Map.of("role", List.of(role)));
    }
}