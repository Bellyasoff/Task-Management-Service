package com.test.task.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@Schema(description = "Token response")
public class JwtAuthenticationResponse {

    @Schema(description = "Access token", example = "")
    private String accessToken;

    @Schema(description = "Refresh token", example = "")
    private String refreshToken;
}
