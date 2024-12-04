package com.test.task.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login data")
public class LoginRequest {
    private String email;
    private String password;
}
