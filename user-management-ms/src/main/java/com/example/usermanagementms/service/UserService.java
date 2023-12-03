package com.example.usermanagementms.service;

import com.example.usermanagementms.domain.*;
import com.example.usermanagementms.dto.SignInRequest;
import com.example.usermanagementms.dto.SignInResponse;
import com.example.usermanagementms.dto.user.UserRequestDto;
import com.example.usermanagementms.dto.user.UserResponseDto;
import com.example.usermanagementms.exception.LoginException;
import com.example.usermanagementms.mapper.GroupMapper;
import com.example.usermanagementms.repository.TokenRepository;
import com.example.usermanagementms.repository.UserRepository;
import com.example.usermanagementms.security.JwtAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final GroupMapper mapper = GroupMapper.INSTANCE;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAuthService jwtAuthService;
    private final TokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(email);
    }

    @Transactional
    public String saveUser(UserRequestDto userDto, Role role){
        userDto.setPassword(passwordEncoder(userDto.getPassword()));
        Authority authority = authorityMaker(role);
        var user = userMaker(userDto, authority);
        authority.setUser(user);
        userRepository.save(user);
        log.info("check2: {}", passwordEncoder.matches(userDto.getPassword(), user.getPassword()));
        return "You create account successfully";
    }

    @Transactional
    public SignInResponse authenticate(SignInRequest request, Role role) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        log.info("authentication: {}", authenticationToken);
        User user = checkIfExists(request.getEmail(), request.getPassword());
        return getSignInResponse(user, role);
    }

    public List<UserResponseDto> getAll(){
        List<User> all = userRepository.findAll();
        return mapper.convertToUserResponseDtos(all);
    }

    public UserResponseDto getById(Long id){
        return mapper.convertToUserResponseDto(userRepository.findById(id).get());
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    private SignInResponse getSignInResponse(User user, Role role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        var accessToken = tokenMaker(authentication, role);
        var tokenEntity = Token.builder().token(accessToken).tokenType(TokenType.ACCESS_TOKEN).user(user).build();

        tokenRepository.save(tokenEntity);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    private User userMaker(UserRequestDto userRequestDto, Authority authority){
        return User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .authorities(Set.of(authority))
                .birthDate(userRequestDto.getBirthDate())
                .build();
    }

    private Authority authorityMaker(Role role){
        return Authority.builder()
                .authority(role)
                .build();
    }

    private String tokenMaker(Authentication authenticationToken, Role role){
        return jwtAuthService.issueToken(authenticationToken, Duration.ofDays(1), role.name());
    }

    private String passwordEncoder(String password){
        String password1 = passwordEncoder.encode(password);
        log.info("Password: {}", password1 );
        log.info("Password check: {}", passwordEncoder.matches(password, password1));
        return password1;
    }

    private User checkIfExists(String username, String password) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        return user;
    }

    private User checkIfExistsDemo(String username, String password) {
        User user = userRepository.findUserByEmail(username).get();
        log.info("check: {}", passwordEncoder.matches(password, user.getPassword()));

        log.info("Password is: " + password);
        log.info("Password is: " + user.getPassword());

        return userRepository.findUserByEmailAndPassword(username, password)
                .orElseThrow(() -> new LoginException());
    }


}
