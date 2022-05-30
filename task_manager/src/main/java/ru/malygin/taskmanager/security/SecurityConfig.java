package ru.malygin.taskmanager.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.malygin.taskmanager.model.entity.Roles;
import ru.malygin.taskmanager.security.filter.CustomAccessDeniedHandler;
import ru.malygin.taskmanager.security.filter.CustomAuthenticationEntryPoint;
import ru.malygin.taskmanager.security.filter.CustomAuthorizationFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  @formatter:off
        http
                .csrf().disable()
                .cors().configurationSource(request -> corsConfiguration())
                .and()
                .logout().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests().anyRequest().hasRole(Roles.USER)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerBean()).authenticationEntryPoint(authenticationEntryPointBean())
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        //  @formatter:on
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.setAllowedMethods(List.of("GET",
                                                "HEAD",
                                                "POST",
                                                "PUT",
                                                "PATCH",
                                                "DELETE",
                                                "OPTIONS",
                                                "TRACE"));
        configuration.setAllowedHeaders(List.of("*"));
        return configuration;
    }

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPointBean() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandlerBean() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
