package ru.malygin.searcher.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.malygin.searcher.security.filter.CustomWebFiler;

@Slf4j
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        //  @formatter:off
        return http
                .csrf().disable()
                .cors().disable()
                .logout().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange().pathMatchers("/api/v1/sse/**", "/api/v1").permitAll()
                .and()
                .authorizeExchange().anyExchange().authenticated()
                .and()
                .addFilterBefore(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
        //  @formatter:on
    }

    @Bean
    public CustomWebFiler webFilter() {
        return new CustomWebFiler(jwtUtil);
    }
}
