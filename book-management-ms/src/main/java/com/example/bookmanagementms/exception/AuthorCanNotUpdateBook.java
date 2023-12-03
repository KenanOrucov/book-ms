package com.example.bookmanagementms.exception;

public class AuthorCanNotUpdateBook extends RuntimeException{

    public static final String MESSAGE = "You can not update this book %s";

    public AuthorCanNotUpdateBook(String bookName) {
        super(String.format(MESSAGE, bookName));
    }
}
