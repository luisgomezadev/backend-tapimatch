package com.lgsoftworks.user.infrastructure.adapter.in.web;

import com.lgsoftworks.user.application.dto.response.UserDTO;
import com.lgsoftworks.user.application.port.in.UploadUserImageUseCase;
import com.lgsoftworks.user.application.port.in.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private final UserUseCase userUseCase;
    private final UploadUserImageUseCase uploadUserImageUseCase;

    @Operation(summary = "Obtener la información del usuario autenticado actualmente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/me")
    public ResponseEntity<Optional<UserDTO>> getCurrentUser() {
        return ResponseEntity.ok(userUseCase.findCurrentUser());
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<UserDTO> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        UserDTO updatedUser = uploadUserImageUseCase.uploadUserImage(id, file);
        return ResponseEntity.ok(updatedUser);
    }

}
