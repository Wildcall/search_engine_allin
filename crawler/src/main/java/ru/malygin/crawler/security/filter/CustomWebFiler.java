package ru.malygin.crawler.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.malygin.crawler.security.JwtUtil;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class CustomWebFiler implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String BEARER = "Bearer ";
        List<String> authHeaderList = request
                .getHeaders()
                .get(AUTHORIZATION);

        if (authHeaderList == null) return chain.filter(exchange);

        String authHeader = authHeaderList
                .stream()
                .findFirst()
                .orElse(null);

        if (authHeader == null || !authHeader.startsWith(BEARER)) return chain.filter(exchange);

        String token = authHeader.substring(BEARER.length());
        UsernamePasswordAuthenticationToken authenticationToken;

        try {
            authenticationToken = jwtUtil.verifyResourceToken(token);
        } catch (Exception e) {
            authenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
        }
        SecurityContextHolder
                .getContext()
                .setAuthentication(authenticationToken);
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
    }
}
