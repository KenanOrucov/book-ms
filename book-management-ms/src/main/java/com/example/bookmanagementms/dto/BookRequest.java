package com.example.bookmanagementms.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    String name;
    String type;
    String author;
    LocalDate releaseDate;
}
