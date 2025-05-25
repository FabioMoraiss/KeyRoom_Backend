package com.br.keyroom.security;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.keyroom.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            String token = JWT.create()
                    .withIssuer("Cassinov1")
                    .withSubject(user.getLogin())
                    .withExpiresAt(getExpirationTime())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException error) {
            throw new RuntimeException("Error while creating token", error);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.require(algorithm)
                    .withIssuer("Cassinov1")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException error) {
           return "";
        }
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
