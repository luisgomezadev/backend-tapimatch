package com.lgsoftworks.user.application.port.in;

import com.lgsoftworks.user.application.dto.request.UserRequest;
import com.lgsoftworks.user.application.dto.response.UserDTO;

import java.util.Optional;

public interface UserUseCase {
    Optional<UserDTO> findById(Long id);

    UserDTO save(UserRequest userRequest);

    Optional<UserDTO> findByEmail(String email);

    Optional<UserDTO> findCurrentUser();
}
