package com.test.task.dto.authDto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
