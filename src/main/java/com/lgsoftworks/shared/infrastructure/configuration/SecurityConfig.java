package com.lgsoftworks.shared.infrastructure.configuration;

import com.lgsoftworks.auth.infrastructure.security.exception.CustomAccessDeniedHandler;
import com.lgsoftworks.auth.infrastructure.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(handlerExceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui-custom.html",
                                "/proxy/**",
                                "/actuator/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/field/p/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/venue/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/venue/code/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservation/code/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservation/availability/hours/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reservation").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reservation/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://projectplaymatch.vercel.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
