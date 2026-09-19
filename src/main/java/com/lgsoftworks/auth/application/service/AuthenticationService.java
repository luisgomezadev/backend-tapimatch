package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.application.dto.request.AuthenticationRequest;
import com.lgsoftworks.auth.application.dto.request.RefreshTokenRequest;
import com.lgsoftworks.auth.application.dto.response.AuthenticationResponse;
import com.lgsoftworks.auth.domain.exception.InvalidCredentialsException;
import com.lgsoftworks.auth.domain.exception.PasswordNotNullException;
import com.lgsoftworks.auth.infrastructure.security.service.JwtService;
import com.lgsoftworks.user.application.dto.mapper.UserModelMapper;
import com.lgsoftworks.user.application.dto.request.UserRequest;
import com.lgsoftworks.user.application.dto.response.UserDTO;
import com.lgsoftworks.user.application.port.in.UserUseCase;
import com.lgsoftworks.user.domain.exception.UserByEmailNotFoundException;
import com.lgsoftworks.user.domain.exception.UserWithEmailExistsException;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.user.domain.port.out.UserRepositoryPort;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.lgsoftworks.user.infrastructure.adapter.out.persistence.mapper.UserDboMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserModelMapper userModelMapper;

    public UserDTO register(UserRequest userRequest) {

        if (userRepositoryPort.findByEmail(userRequest.getEmail()).isPresent())
            throw new UserWithEmailExistsException(userRequest.getEmail());

        if (userRequest.getPassword() == null) {
            throw new PasswordNotNullException();
        }

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return userUseCase.save(userRequest);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        User user = userRepositoryPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserByEmailNotFoundException(request.getEmail()));

        UserEntity userEntity = UserDboMapper.toDbo(user);

        String jwtToken = jwtService.generateToken(userEntity, userEntity.getRole().name());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userModelMapper.toUserDTO(user))
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        User user = userRepositoryPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserByEmailNotFoundException(request.getEmail()));

        String newToken = jwtService.renewToken(request.getToken(), userEntityToUserDetails(user));

        return AuthenticationResponse.builder()
                .token(newToken)
                .user(userModelMapper.toUserDTO(user))
                .build();
    }

    private UserDetails userEntityToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }

}
