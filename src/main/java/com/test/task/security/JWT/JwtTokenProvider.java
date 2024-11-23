package com.test.task.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.task.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private long validityInMilliseconds = 360000;

    // Генерация токена
    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    // Проверка валидности токена
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Token verification failed: " + e.getMessage());
            return false;
        }
    }

    // Получение email из токена
    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }
}
