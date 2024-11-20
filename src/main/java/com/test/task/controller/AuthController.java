package com.test.task.controller;

//import com.test.task.dto.LoginRequest;
import com.test.task.dto.UserDto;
//import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
//    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController (UserService userService/*, JwtTokenProvider jwtTokenProvider*/) {
        this.userService = userService;
    //    this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        UserDto user = userService.findByEmail(loginRequest.getEmail());
//            // Генерация JWT токена
//            String token = jwtTokenProvider.createToken(loginRequest.getEmail());
//            return ResponseEntity.ok(token);
//    }
}
