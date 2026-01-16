package com.project.chatservice.config;

import com.project.chatservice.auth.config.AuthProperties;
import com.project.chatservice.auth.jwt.JwtAuthenticationFilter;
import com.project.chatservice.auth.jwt.JwtService;
import com.project.chatservice.auth.session.AuthSessionStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Represents the security config.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthProperties authProperties,
                                                   JwtService jwtService,
                                                   AuthSessionStore sessionStore) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, sessionStore);
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/api/auth/**", "/ws/**").permitAll();
                if (authProperties.enforce()) {
                    authorize.anyRequest().authenticated();
                } else {
                    authorize.anyRequest().permitAll();
                }
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
