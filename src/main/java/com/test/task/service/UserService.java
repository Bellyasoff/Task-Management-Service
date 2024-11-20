package com.test.task.service;

//import com.test.task.dto.LoginRequest;
import com.test.task.dto.UserDto;
import com.test.task.model.UserEntity;

public interface UserService {
    void saveUser(UserDto userDto);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

//    String authenticate(LoginRequest loginRequest);

//    boolean passwordMatches(String rawPassword, String encodedPassword);
}
