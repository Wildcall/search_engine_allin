package ru.malygin.crawler.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import ru.malygin.crawler.security.filter.CustomWebFiler;

import java.util.List;

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
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CustomWebFiler webFilter() {
        return new CustomWebFiler(jwtUtil);
    }
}
