package com.test.task.controller;

import com.test.task.dto.JwtAuthenticationResponse;
import com.test.task.dto.LoginRequest;
import com.test.task.dto.UserDto;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController (UserService userService,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

            // Генерация JWT токена
            String token = userService.authenticate(loginRequest);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}
