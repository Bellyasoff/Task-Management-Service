package com.test.task.dto.authDto;

import lombok.Data;

@Data
public class RefreshJwtRequest {

    private String refreshToken;
}
