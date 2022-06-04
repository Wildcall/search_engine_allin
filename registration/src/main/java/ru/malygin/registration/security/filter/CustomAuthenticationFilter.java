package ru.malygin.registration.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.malygin.registration.exception.ApiError;
import ru.malygin.registration.model.AuthResponse;
import ru.malygin.registration.model.entity.AppUser;
import ru.malygin.registration.model.entity.Role;
import ru.malygin.registration.security.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("LOGIN / Email: {}", email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                                                                                                          password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(appUser);
        String refreshToken = jwtUtil.generateRefreshToken(appUser);
        AuthResponse authResponse = new AuthResponse(accessToken,
                                                     refreshToken,
                                                     appUser.getId(),
                                                     appUser.getEmail(),
                                                     appUser
                                                             .getRoles()
                                                             .stream()
                                                             .map(Role::getName)
                                                             .toList());

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        String email = request.getParameter("email");
        log.error("Failed login! Email: {} / Cause: {}", email, failed.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Email or password is invalid");
        ApiError.writeApiError(response, apiError);
    }
}
