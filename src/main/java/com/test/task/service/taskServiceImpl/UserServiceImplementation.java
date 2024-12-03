package com.test.task.service.taskServiceImpl;

import com.test.task.dto.UserDto;
import com.test.task.model.Role;
import com.test.task.model.UserEntity;
import com.test.task.repository.RoleRepository;
import com.test.task.repository.UserRepository;
import com.test.task.security.CustomUserDetailService;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.test.task.mapper.UserMapper.mapToUser;
import static com.test.task.mapper.UserMapper.mapToUserDto;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService userDetailService;

    @Autowired
    public UserServiceImplementation (UserRepository userRepository,
                                      RoleRepository roleRepository,
                                      JwtTokenProvider jwtTokenProvider,
                                      PasswordEncoder passwordEncoder,
                                      CustomUserDetailService userDetailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
    }

    @Override
    public void saveUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email already exists: " + userDto.getEmail());
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username already exists: " + userDto.getUsername());
        }

        UserEntity user = mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow();
        if (user == null) {
            throw new IllegalArgumentException("User with username not found: " + username);
        }
        return mapToUserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow();
        if (user == null) {
            throw new IllegalArgumentException("User with email not found: " + email);
        }
        return mapToUserDto(user);
    }

    @Override
    public UserDto findById(long userId) throws Exception {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id: " + userId));
        return mapToUserDto(user);
    }

    @Override
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
