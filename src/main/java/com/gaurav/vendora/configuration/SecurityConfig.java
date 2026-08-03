package com.gaurav.vendora.configuration;

import com.gaurav.vendora.configuration.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public @NonNull CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration();

                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:5173",
                        "http://localhost:3000",
                        "https://vendora-ui.vercel.app",
                        "https://vendora-glzrto7ua-gauravsd-gits-projects.vercel.app"
                ));

                cfg.setAllowedMethods(Arrays.asList(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS"
                ));

                cfg.setAllowCredentials(true);

                cfg.setAllowedHeaders(Collections.singletonList("*"));

                cfg.setExposedHeaders(List.of("Authorization"));

                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }
}