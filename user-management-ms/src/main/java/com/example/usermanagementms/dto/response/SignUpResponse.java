package com.example.usermanagementms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private String message;
}