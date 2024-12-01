package com.test.task.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.task.model.UserEntity;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.access}")
    private String jwtAccessToken;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshToken;
    private final long accessExpiration = 90000;
    @Getter
    private final long refreshExpiration = 2592000000L;

    // Генерация access токена
    public String generateAccessToken(String email) {
        final Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpiration);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(jwtAccessToken));
    }

    // Генерация refresh токена
    public String generateRefreshToken(String email) {
        final Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpiration);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(jwtRefreshToken));
    }

    // Проверка валидности access токена
    public boolean validateAccessToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtAccessToken)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Access Token verification failed: " + e.getMessage());
            return false;
        }
    }
    // Проверка валидности refresh токена
    public boolean validateRefreshToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtRefreshToken)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Refresh token verification failed: " + e.getMessage());
            return false;
        }
    }

    // Получение email из токена
    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }
}
