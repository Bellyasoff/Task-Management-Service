package com.test.task.service.taskServiceImpl;

import com.test.task.dto.UserDto;
import com.test.task.model.Role;
import com.test.task.model.UserEntity;
import com.test.task.repository.RoleRepository;
import com.test.task.repository.UserRepository;
import com.test.task.security.CustomUserDetailService;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.test.task.mapper.UserMapper.mapToUser;
import static com.test.task.mapper.UserMapper.mapToUserDto;

@Service
@Log4j
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
        log.info("Saving user:\nUsername - " + userDto.getUsername()  +
                ",\nEmail - " + userDto.getEmail());
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.error("User with email already exists: " + userDto.getEmail());
            throw new IllegalArgumentException("User with email already exists: " + userDto.getEmail());
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            log.error("User with username already exists: " + userDto.getUsername());
            throw new IllegalArgumentException("User with username already exists: " + userDto.getUsername());
        }

        UserEntity user = mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singletonList(role));
        log.info("Setting role: " + user.getRoles());

        userRepository.save(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        log.info("Trying to find user with username: " + username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username not found: " + username);
                    throw new IllegalArgumentException("User with username not found: " + username);
                });
        return mapToUserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        log.info("Trying to find user with email: " + email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email not found: " + email);
                    throw new IllegalArgumentException("User with email not found: " + email);
                });
        return mapToUserDto(user);
    }

    @Override
    public UserDto findById(long userId) throws Exception {
        log.info("Trying to find user by id: " + userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with userId not found: " + userId);
                    throw new IllegalArgumentException("User with userId not found: " + userId);
                });
        return mapToUserDto(user);
    }

    @Override
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
