package ru.malygin.registration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.malygin.registration.model.Roles;
import ru.malygin.registration.security.filter.CustomAuthenticationEntryPoint;
import ru.malygin.registration.security.filter.CustomAccessDeniedHandler;
import ru.malygin.registration.security.filter.CustomAuthenticationFilter;
import ru.malygin.registration.security.filter.CustomAuthorizationFilter;
import ru.malygin.registration.service.impl.AppUserServiceImpl;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserServiceImpl appUserServiceImpl;
    private final PasswordEncoder BCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(appUserServiceImpl)
                .passwordEncoder(BCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManagerBean(), jwtUtil);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");

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
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/user/registration").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/user").hasAnyRole(Roles.NEW, Roles.USER, Roles.ADMIN)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/user/confirm").hasRole(Roles.CONFIRM)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/user/refresh").hasRole(Roles.REFRESH)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/user/resend").hasRole(Roles.NEW)
                .and()
                .authorizeRequests().anyRequest().denyAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerBean()).authenticationEntryPoint(authenticationEntryPointBean())
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        //  @formatter:on
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        return configuration;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
