package com.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "User data")
public class UserDto {
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String password;
}
