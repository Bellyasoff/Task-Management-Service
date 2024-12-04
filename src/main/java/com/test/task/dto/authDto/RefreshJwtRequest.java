package com.test.task.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Refresh token data")
public class RefreshJwtRequest {

    private String refreshToken;
}
