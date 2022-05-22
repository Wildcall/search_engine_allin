package ru.malygin.notification.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.malygin.notification.config.ResourceConfig;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {

    private final ResourceConfig resourceConfig;

    public UsernamePasswordAuthenticationToken verify(@NotNull String token) {
        Algorithm algorithm = Algorithm.HMAC256(resourceConfig
                                                        .getSecret()
                                                        .getBytes(StandardCharsets.UTF_8));
        JWTVerifier jwtVerifier = JWT
                .require(algorithm)
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String username = decodedJWT.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }
}
