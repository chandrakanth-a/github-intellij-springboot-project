package com.example.apigw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF for APIs
                .csrf(csrf -> csrf.disable())
                // authorize requests
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI and API docs should be public
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Public endpoints like register & login
                        .requestMatchers("/auth/**", "/register", "/login").permitAll()
                        // everything else requires authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
