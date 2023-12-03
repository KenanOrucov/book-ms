package com.example.usermanagementms.exception;

public class LoginException extends RuntimeException {


    public static final String MESSAGE = "Email or password is incorrect";

    public LoginException() {
        super(String.format(MESSAGE));
    }
}
