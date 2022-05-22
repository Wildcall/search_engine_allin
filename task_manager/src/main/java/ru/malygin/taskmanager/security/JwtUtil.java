package ru.malygin.taskmanager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.config.ResourceConfig;
import ru.malygin.taskmanager.model.ResourceType;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {

    private final ResourceConfig resourceConfig;

    public UsernamePasswordAuthenticationToken verifyAccessToken(@NotNull String token,
                                                                 ResourceType type) {
        Algorithm algorithm = Algorithm.HMAC256(resourceConfig
                                                        .getResource(type)
                                                        .getSecretKey()
                                                        .getBytes(StandardCharsets.UTF_8));
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

    public UsernamePasswordAuthenticationToken verifyCallbackToken(@NotNull String token) {
        Algorithm algorithm = Algorithm.HMAC256(resourceConfig
                                                        .getSecret()
                                                        .getBytes(StandardCharsets.UTF_8));
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

    public String generateResourceToken(ResourceType type) {
        Algorithm algorithm = Algorithm.HMAC256(resourceConfig
                                                        .getResource(type)
                                                        .getSecretKey()
                                                        .getBytes(StandardCharsets.UTF_8));
        return JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000))
                .sign(algorithm);
    }
}
