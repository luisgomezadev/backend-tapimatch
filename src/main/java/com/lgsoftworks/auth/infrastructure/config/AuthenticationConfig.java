package com.lgsoftworks.auth.infrastructure.config;

import com.lgsoftworks.auth.domain.model.AuthenticatedUser;
import com.lgsoftworks.user.domain.exception.UserByEmailNotFoundException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserRepositoryPort userRepositoryPort;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {

            User user = userRepositoryPort.findByEmail(username)
                    .orElseThrow(() ->
                            new UserByEmailNotFoundException(username)
                    );

            return new AuthenticatedUser(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    List.of(
                            new SimpleGrantedAuthority(
                                    user.getRole().name()
                            )
                    )
            );
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
