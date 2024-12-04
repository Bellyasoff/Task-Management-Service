package com.test.task.service.taskServiceImpl;

import com.test.task.dto.authDto.JwtAuthenticationResponse;
import com.test.task.dto.authDto.LoginRequest;
import com.test.task.model.RefreshStorage;
import com.test.task.repository.RefreshStorageRepository;
import com.test.task.security.CustomUserDetailService;
import com.test.task.security.JWT.JwtTokenProvider;
import com.test.task.service.AuthService;
import com.test.task.service.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImplementation implements AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshStorageRepository refreshStorageRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;

    public AuthServiceImplementation(UserService userService,
                                     JwtTokenProvider jwtTokenProvider,
                                     RefreshStorageRepository refreshStorageRepository,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailService userDetailService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshStorageRepository = refreshStorageRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {

        final String accessToken = jwtTokenProvider.generateAccessToken(loginRequest.getEmail());
        final String refreshToken = jwtTokenProvider.generateRefreshToken(loginRequest.getEmail());
        saveRefreshToken(loginRequest.getEmail(), refreshToken, jwtTokenProvider.getRefreshExpiration());

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public JwtAuthenticationResponse refresh(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            Optional<RefreshStorage> refreshTokenEntityOpt = refreshStorageRepository.findByToken(refreshToken);

            if (refreshTokenEntityOpt.isPresent()) {
                RefreshStorage refreshStorageEntity = refreshTokenEntityOpt.get();

                if (refreshStorageEntity.getToken().equals(refreshToken)) {
                    final String email = jwtTokenProvider.getEmailFromToken(refreshToken);

                    final String accessToken = jwtTokenProvider.generateAccessToken(email);

                    return new JwtAuthenticationResponse(accessToken, null);
                }
            }
        }
        try {
            throw new AuthException("JWT token not valid");
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JwtAuthenticationResponse getNewAccessToken(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            Optional<RefreshStorage> refreshTokenEntityOpt = refreshStorageRepository.findByToken(refreshToken);

            if (refreshTokenEntityOpt.isPresent()) {
                RefreshStorage refreshStorageEntity = refreshTokenEntityOpt.get();

                if (refreshStorageEntity.getToken().equals(refreshToken)) {
                    final String email = jwtTokenProvider.getEmailFromToken(refreshToken);

                    final String accessToken = jwtTokenProvider.generateAccessToken(email);
                    final String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

                    refreshStorageEntity.setToken(newRefreshToken);
                    refreshStorageRepository.save(refreshStorageEntity);

                    return new JwtAuthenticationResponse(accessToken, newRefreshToken);
                }
            }
        }
        return new JwtAuthenticationResponse(null, null);
    }

    private void saveRefreshToken(String email, String token, long validity) {
        final Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        RefreshStorage existingToken = refreshStorageRepository.findByEmail(email);

        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(expiryDate);
            refreshStorageRepository.save(existingToken);
        } else {
            RefreshStorage refreshTokenData = new RefreshStorage(email, token, expiryDate);
            refreshStorageRepository.save(refreshTokenData);
        }
    }

}
