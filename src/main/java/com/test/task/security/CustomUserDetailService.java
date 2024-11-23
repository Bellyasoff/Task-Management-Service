package com.test.task.security;

import com.test.task.model.UserEntity;
import com.test.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("userDetailService")
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null) {
            return new CustomUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map(role -> new SimpleGrantedAuthority(
                            role.getName())).collect(Collectors.toList())

            );
        } else {
            throw new UsernameNotFoundException("User not found " + username);
        }
    }
}
