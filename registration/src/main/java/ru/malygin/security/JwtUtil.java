package ru.malygin.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.malygin.config.ResourceConfig;
import ru.malygin.model.ResourceType;
import ru.malygin.model.entity.AppUser;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtUtil {

    private final ResourceConfig resourceConfig;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtUtil(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
        this.algorithm = Algorithm.HMAC256(resourceConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.jwtVerifier = JWT
                .require(algorithm)
                .build();
    }

    public UsernamePasswordAuthenticationToken verifyToken(@NotNull String token) {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String username = decodedJWT.getSubject();
        List<SimpleGrantedAuthority> authority = decodedJWT
                .getClaim("roles")
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(username, null, authority);
    }

    public String generateAccessToken(@NotNull AppUser appUser) {
        List<String> roles = appUser
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return generateToken(appUser, roles, resourceConfig.getExpiration().getAccess());
    }

    public String generateRefreshToken(@NotNull AppUser appUser) {
        List<String> roles = List.of("REFRESH");
        return generateToken(appUser, roles, resourceConfig.getExpiration().getRefresh());
    }

    public String generateConfirmToken(@NotNull AppUser appUser) {
        List<String> roles = List.of("CONFIRM");
        return generateToken(appUser, roles, resourceConfig.getExpiration().getConfirm());
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

    private String generateToken(@NotNull AppUser subject,
                                 List<String> roles,
                                 @NotNull Long expirationTime) {
        return JWT
                .create()
                .withSubject(subject.getEmail())
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }
}
