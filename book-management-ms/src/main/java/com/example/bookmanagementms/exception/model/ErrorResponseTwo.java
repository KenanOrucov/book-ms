package com.example.bookmanagementms.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ErrorResponseTwo {
    private Integer httpStatus;
    private final String message;

}