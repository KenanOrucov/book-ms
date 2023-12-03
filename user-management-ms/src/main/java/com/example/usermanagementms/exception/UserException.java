package com.example.usermanagementms.exception;

public class UserException extends RuntimeException{


    public static final String MESSAGE = "User %s does not exist";

    public UserException(Long bookId) {
        super(String.format(MESSAGE, bookId));
    }
}
