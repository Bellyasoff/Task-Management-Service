package com.test.task.service.taskServiceImpl;

//import com.test.task.dto.LoginRequest;
import com.test.task.dto.UserDto;
import com.test.task.model.Role;
import com.test.task.model.UserEntity;
import com.test.task.repository.RoleRepository;
import com.test.task.repository.UserRepository;
//import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.test.task.mapper.UserMapper.mapToUser;
import static com.test.task.mapper.UserMapper.mapToUserDto;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final JwtTokenProvider jwtTokenProvider;

//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation (UserRepository userRepository,
                                      RoleRepository roleRepository/*,
                                      JwtTokenProvider jwtTokenProvider,
                                      PasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
//        this.jwtTokenProvider = jwtTokenProvider;
 //       this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new IllegalArgumentException("User with email already exists: " + userDto.getEmail());
        }
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentException("User with username already exists: " + userDto.getUsername());
        }

        UserEntity user = mapToUser(userDto);
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("USER");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username not found: " + username);
        }
        return mapToUserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email not found: " + email);
        }
        return mapToUserDto(user);
    }

//    @Override
//    public String authenticate(LoginRequest loginRequest) {
//        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());
//
//        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Invalid email or password");
//        }
//
//        return jwtTokenProvider.createToken(loginRequest.getEmail());
//    }
//
//    @Override
//    public boolean passwordMatches(String rawPassword, String encodedPassword) {
//        return passwordEncoder.matches(rawPassword, encodedPassword);
//    }
}
