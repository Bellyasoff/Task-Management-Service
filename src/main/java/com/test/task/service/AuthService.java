package com.test.task.service;

import com.test.task.dto.authDto.JwtAuthenticationResponse;
import com.test.task.dto.authDto.LoginRequest;

public interface AuthService {

    JwtAuthenticationResponse login(LoginRequest loginRequest);
    JwtAuthenticationResponse refresh(String refreshToken);

    JwtAuthenticationResponse getNewAccessToken(String refreshToken);
}
