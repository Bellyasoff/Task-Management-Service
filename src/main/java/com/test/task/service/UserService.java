package com.test.task.service;

import com.test.task.dto.JwtAuthenticationResponse;
import com.test.task.dto.LoginRequest;
import com.test.task.dto.UserDto;

import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    UserDto findById(long id) throws Exception;

    boolean passwordMatches(String rawPassword, String encodedPassword);
}
