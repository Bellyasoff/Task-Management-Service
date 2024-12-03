package com.test.task.controller;

import com.test.task.dto.authDto.JwtAuthenticationResponse;
import com.test.task.dto.authDto.LoginRequest;
import com.test.task.dto.authDto.RefreshJwtRequest;
import com.test.task.dto.UserDto;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.AuthService;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Autowired
    public AuthController (UserService userService,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager,
                           AuthService authService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = authService.login(loginRequest);
        System.out.println("ACCESS TOKEN: " + response.getAccessToken());
        System.out.println("REFRESH TOKEN: " + response.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    // Получить новый access токен
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshAccessToken(@RequestBody RefreshJwtRequest refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken.getRefreshToken()));
    }

    // Получить новый refresh и access токены
    @PostMapping("/token")
    public ResponseEntity<JwtAuthenticationResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshToken) {
        return ResponseEntity.ok(authService.getNewAccessToken(refreshToken.getRefreshToken()));
    }
}