package com.example.bookmanagementms.exception;

public class BookException extends RuntimeException{


    public static final String MESSAGE = "Book %s does not exist";

    public BookException(Long bookId) {
        super(String.format(MESSAGE, bookId));
    }
}
