package com.test.task.service;

import com.test.task.dto.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    UserDto findById(long id) throws Exception;

    boolean passwordMatches(String rawPassword, String encodedPassword);
}
