package com.example.usermanagementms.service;

import com.example.usermanagementms.domain.*;
import com.example.usermanagementms.dto.request.SignInRequest;
import com.example.usermanagementms.dto.request.UserRequestDto;
import com.example.usermanagementms.dto.response.SignInResponse;
import com.example.usermanagementms.dto.response.SignUpResponse;
import com.example.usermanagementms.dto.response.UserResponseDto;
import com.example.usermanagementms.exception.NotFoundException;
import com.example.usermanagementms.mapper.ManualMapper;
import com.example.usermanagementms.repository.TokenRepository;
import com.example.usermanagementms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.example.usermanagementms.domain.TokenType.ACCESS_TOKEN;
import static com.example.usermanagementms.domain.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
//    private final GroupMapper mapper = GroupMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;
    private final ManualMapper mapper;
    private final JwtService jwtAuthService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public List<UserResponseDto> getAll(){
        List<User> all = userRepository.findAll();
        return mapper.convertToUserResponseDtos(all);
    }

    public UserResponseDto getById(Long id){
        return mapper.convertToUserResponseDto(userRepository.findById(id).get());
    }

    public SignUpResponse saveUser(UserRequestDto request, Role role) {
        Authority authority = authorityMaker(role);
        userMaker(request, authority);
        return SignUpResponse.builder()
                .message("Your registration has been successfully completed.")
                .build();
    }

    @Transactional
    public SignInResponse authenticate(SignInRequest request, Role role) {
        var result = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        log.info("authentication: {}", result);
        User user = checkIfExists(request.getUsername(), request.getPassword());
        return getSignInResponse(user, role);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    private User checkIfExists(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Username or password is incorrect"));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new NotFoundException("Username or password is incorrect");
        }
        return user;
    }

    private SignInResponse getSignInResponse(User user, Role role) {
        var accessToken = jwtAuthService.generateToken(user, role);
        var refreshToken = jwtAuthService.generateRefreshToken(user, role);
        revokeAllUserTokens(user);
        saveTokens(user, accessToken, refreshToken);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveTokens(User user, String accessToken, String refreshToken) {
        saveUserToken(user, accessToken, ACCESS_TOKEN);
        saveUserToken(user, refreshToken, REFRESH_TOKEN);
    }

    private void saveUserToken(User user, String jwtToken, TokenType type) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(type)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private Authority authorityMaker(Role role){
        return Authority.builder()
                .authority(role)
                .build();
    }

    private User userMaker(UserRequestDto userRequestDto, Authority authority){
         return userRepository.save(User.builder()
                .username(userRequestDto.getUsername())
                .lastName(userRequestDto.getLastName())
                .firstName(userRequestDto.getFirstName())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .authorities(Set.of(authority))
                .age(userRequestDto.getAge())
                .build());
    }

}
