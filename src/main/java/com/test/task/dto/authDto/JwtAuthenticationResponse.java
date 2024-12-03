package com.test.task.dto.authDto;

import lombok.*;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;
    private String refreshToken;
}
