package com.example.bookmanagementms.exception;

import com.example.bookmanagementms.exception.model.ErrorResponseTwo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(BookException.class)
    public ResponseEntity<ErrorResponseTwo> bookExceptionHandler(BookException ex){
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponseTwo(NOT_FOUND.value(), ex.getMessage()));
    }


    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(AuthorCanNotDeleteBook.class)
    public ResponseEntity<ErrorResponseTwo> authorCanNotDeleteBookHandler(AuthorCanNotDeleteBook ex){
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponseTwo(NOT_ACCEPTABLE.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponseTwo> handleException(CustomException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponseTwo(BAD_REQUEST.value(), exception.getMessage()));
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseTwo> sqlException(SQLException ex){
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponseTwo(NOT_ACCEPTABLE.value(), ex.getMessage()));
    }

}
