package com.sgyjdev.studyolle.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> {
            request.requestMatchers(antMatcher("/"), antMatcher("/login"), antMatcher("/sign-up"), antMatcher("/check-email"), antMatcher("/check-email-token"),
                antMatcher("/email-login"), antMatcher("/check-email-login"), antMatcher("/login-link")).permitAll();

            request.requestMatchers(antMatcher(HttpMethod.GET), antMatcher("/profile/*")).permitAll();
        });
        return http.build();
    }

}
