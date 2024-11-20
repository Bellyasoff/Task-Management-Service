package com.test.task.mapper;

import com.test.task.dto.UserDto;
import com.test.task.model.UserEntity;

public class UserMapper {

    public static UserEntity mapToUser(UserDto userDto) {
        UserEntity user = UserEntity.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();

        return user;
    }

    public static UserDto mapToUserDto(UserEntity userEntity) {
        UserDto user = UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();

        return user;
    }
}
