package ru.malygin.taskmanager.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.security.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String BEARER = "Bearer ";

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER.length());
        UsernamePasswordAuthenticationToken authenticationToken;
        try {
            if (request.getRequestURI().endsWith("callback"))
                authenticationToken = jwtUtil.verifyCallbackToken(token);
            else
                authenticationToken = jwtUtil.verifyAccessToken(token, ResourceType.REGISTRATION);
        } catch (Exception e) {
            authenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
        }
        SecurityContextHolder
                .getContext()
                .setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
