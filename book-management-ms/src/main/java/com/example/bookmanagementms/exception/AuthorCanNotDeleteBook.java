package com.example.bookmanagementms.exception;

public class AuthorCanNotDeleteBook extends RuntimeException{

    public static final String MESSAGE = "You can not delete this book %s";

    public AuthorCanNotDeleteBook(String bookName) {
        super(String.format(MESSAGE, bookName));
    }
}
