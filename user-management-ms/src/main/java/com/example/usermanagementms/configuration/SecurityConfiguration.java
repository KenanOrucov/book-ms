package com.example.usermanagementms.configuration;

import com.example.usermanagementms.filter.JwtAuthFilterConfigurerAdapter;
import com.example.usermanagementms.security.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final List<AuthService> authServices;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.apply(new JwtAuthFilterConfigurerAdapter(authServices));

        http.authorizeRequests().mvcMatchers("/v1/user/register/**").permitAll()
                .mvcMatchers("/v1/book/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/book").hasAnyAuthority("AUTHOR")
                .antMatchers(HttpMethod.PUT, "/v1/book/**").hasAnyAuthority("AUTHOR")
                .antMatchers(HttpMethod.DELETE, "/v1/book/**").hasAnyAuthority("AUTHOR")
                .mvcMatchers("/v1/user/login/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/user/api").hasAnyAuthority("USER");
//                .mvcMatchers("/students").hasAnyAuthority("STUDENT")
//                .mvcMatchers("/authors").hasAnyAuthority("AUTHOR");
        super.configure(http);
    }
}
