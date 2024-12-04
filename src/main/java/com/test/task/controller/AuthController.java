package com.test.task.controller;

import com.test.task.dto.authDto.JwtAuthenticationResponse;
import com.test.task.dto.authDto.LoginRequest;
import com.test.task.dto.authDto.RefreshJwtRequest;
import com.test.task.dto.UserDto;
import com.test.task.dto.taskDto.TaskDto;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.AuthService;
import com.test.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
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

    @ApiResponse(responseCode = "204", description = "User registered")
    @Operation(summary = "User registration")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200", description = "User login",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))
            })
    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // Получить новый access токен
    @ApiResponse(responseCode = "200", description = "New access token created",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))
            })
    @Operation(summary = "Get new access token")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshAccessToken(@RequestBody RefreshJwtRequest refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken.getRefreshToken()));
    }

    // Получить новый refresh и access токены
    @ApiResponse(responseCode = "200", description = "New access and refresh tokens created",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))
            })
    @Operation(summary = "Get new access and refresh tokens")
    @PostMapping("/token")
    public ResponseEntity<JwtAuthenticationResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshToken) {
        return ResponseEntity.ok(authService.getNewAccessToken(refreshToken.getRefreshToken()));
    }
}