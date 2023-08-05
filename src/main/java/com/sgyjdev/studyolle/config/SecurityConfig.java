package com.sgyjdev.studyolle.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> {
            request.requestMatchers(antMatcher("/"), antMatcher("/login"), antMatcher("/sign-up"), antMatcher("/check-email-token"), antMatcher("/email-login"),
                antMatcher("/check-email-login"), antMatcher("/login-link")).permitAll();

            request.requestMatchers(antMatcher(HttpMethod.GET), antMatcher("/profile/*")).permitAll();
            request.anyRequest().authenticated();


        });
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(antMatcher("/node_modules/**")).requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
