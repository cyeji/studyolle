package com.sgyjdev.studyolle.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.sgyjdev.studyolle.security.CustomAuthenticationProvider;
import com.sgyjdev.studyolle.security.CustomLoginSuccessHandler;
import com.sgyjdev.studyolle.security.CustomUsernamePasswordAuthenticationFilter;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> {
//            request.requestMatchers(antMatcher("/"), antMatcher("/email-login"), antMatcher("/check-email-login"), antMatcher("/login-link")).permitAll();
            request.requestMatchers(antMatcher(HttpMethod.GET), antMatcher("/profile/*")).permitAll();
            request.requestMatchers(antMatcher("/sign-up"), antMatcher("/check-email-token")).permitAll();
            request.anyRequest().authenticated();
        });
        http.formLogin(formLogin -> formLogin.loginPage("/login"));
        http.logout(logout -> logout.logoutSuccessUrl("/"));
        http.addFilterBefore(customUsernamePasswordFilter(http.getSharedObject(AuthenticationConfiguration.class)), UsernamePasswordAuthenticationFilter.class);
        http.rememberMe(rememberMe -> rememberMe.userDetailsService(userDetailsService).tokenRepository(tokenRepository()));
        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(
            authenticationManager(authenticationConfiguration));
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customUsernamePasswordAuthenticationFilter.afterPropertiesSet();
        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(antMatcher("/node_modules/**")).requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
