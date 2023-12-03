package com.example.usermanagementms.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;


@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ErrorResponse {
    String message;
}

