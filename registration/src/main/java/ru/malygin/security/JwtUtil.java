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
import ru.malygin.model.entity.AppUser;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtUtil {

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    @Value("${resource.exp.access}")
    private Long accessExpTime;
    @Value("${resource.exp.refresh}")
    private Long refreshExpTime;
    @Value("${resource.exp.confirm}")
    private Long confirmExpTime;

    public JwtUtil(@Value("${resource.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
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
        log.info("VERIFY {} TOKEN / Email: {}", authority, username);
        return new UsernamePasswordAuthenticationToken(username, null, authority);
    }

    public String generateAccessToken(@NotNull AppUser appUser) {
        List<String> roles = appUser
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        log.info("GENERATE ACCESS TOKEN / Email: {}", appUser.getEmail());
        return generateToken(appUser, roles, accessExpTime);
    }

    public String generateRefreshToken(@NotNull AppUser appUser) {
        List<String> roles = List.of("REFRESH");
        log.info("GENERATE REFRESH TOKEN / Email: {}", appUser.getEmail());
        return generateToken(appUser, roles, refreshExpTime);
    }

    public String generateConfirmToken(@NotNull AppUser appUser) {
        List<String> roles = List.of("CONFIRM");
        log.info("GENERATE CONFIRM TOKEN / Email: {}", appUser.getEmail());
        return generateToken(appUser, roles, confirmExpTime);
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
