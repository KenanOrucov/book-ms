package com.example.bookmanagementms.dto;

import com.example.usermanagementms.dto.user.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDetailResponse {
    private List<BookResponseDto> bookResponseDto;
    private UserResponseDto userResponseDto;
}
