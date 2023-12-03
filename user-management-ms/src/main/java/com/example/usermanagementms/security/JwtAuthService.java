package com.example.usermanagementms.security;

import com.example.usermanagementms.filter.SecurityProperties;
import com.example.usermanagementms.util.HttpConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.usermanagementms.util.HttpConstants.BEARER_AUTH_HEADER;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthService implements AuthService{

    private final SecurityProperties securityProperties;
    private static final String ROLE_CLAIM ="rule";
    Key key;

    @Override
    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest) {
        Optional<String> jwtToken = Optional.ofNullable(httpServletRequest.getHeader(HttpConstants.AUTH_HEADER));
        log.trace("The token is {}", jwtToken);
        return Optional.ofNullable(httpServletRequest.getHeader(HttpConstants.AUTH_HEADER))
                .filter(this::isBearerAuth)
                .flatMap(this::getAuthenticationBearer);
    }

    @PostConstruct
    private void init(){
        byte[] keyBytes = Decoders.BASE64.decode(securityProperties.getJwtProperties().getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isBearerAuth(String header) {
        return header.toLowerCase().startsWith(BEARER_AUTH_HEADER.toLowerCase());
    }

    private Optional<Authentication> getAuthenticationBearer(String header) {
        String token = header.substring(BEARER_AUTH_HEADER.length()).trim();
        log.trace("The token is {}", token);
        Claims claims = parseToken(token);
        log.trace("The claims parsed {}", claims);
        return Optional.of(getAuthenticationBearer(claims));
//        return Optional.empty();
    }

    private Authentication getAuthenticationBearer(Claims claims) {
        List<?> roles = claims.get(ROLE_CLAIM, List.class);
        List<GrantedAuthority> authorityList = roles
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.toString()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorityList);
    }

    private Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
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

        public String issueToken(Authentication authenticationToken, Duration ofDays, String role) {
        log.trace("Issue token to {} for {}", authenticationToken.getPrincipal(), ofDays);
        log.trace("issueToken authorities: {}", authenticationToken.getAuthorities());
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(authenticationToken.getName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(ofDays)))
                .signWith(key, SignatureAlgorithm.HS512);

        addclaimsSet(jwtBuilder, authenticationToken, role);
        log.trace("issueToken authorities: {}", authenticationToken.getAuthorities());
        return jwtBuilder.compact();
    }

    private void addclaimsSet(JwtBuilder jwtBuilder, Authentication authenticationToken, String role) {
        Collection<? extends GrantedAuthority> authorities = authenticationToken.getAuthorities();
        List rules = authorities.stream().map(a-> a.getAuthority()).collect(Collectors.toList());
        log.trace("rule: {}", authorities);
        jwtBuilder.addClaims(Map.of("rule", List.of(role)));
    }
}
