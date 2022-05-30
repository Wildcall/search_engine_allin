package ru.malygin.taskmanager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(@Value("${resource.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public UsernamePasswordAuthenticationToken verifyAccessToken(@NotNull String token) {
        DecodedJWT decodedJWT = JWT
                .require(algorithm)
                .build()
                .verify(token);
        String username = decodedJWT.getSubject();
        List<SimpleGrantedAuthority> authority = decodedJWT
                .getClaim("roles")
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(username, null, authority);
    }
}
