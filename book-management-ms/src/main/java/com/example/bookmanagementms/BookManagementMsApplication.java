package com.example.bookmanagementms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
public class BookManagementMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookManagementMsApplication.class, args);
    }

}
