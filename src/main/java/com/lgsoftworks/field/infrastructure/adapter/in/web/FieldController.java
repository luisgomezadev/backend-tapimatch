package com.lgsoftworks.field.infrastructure.adapter.in.web;

import com.lgsoftworks.field.application.dto.request.FieldRequest;
import com.lgsoftworks.field.application.dto.response.FieldDTO;
import com.lgsoftworks.field.application.dto.response.PublicFieldDTO;
import com.lgsoftworks.field.application.port.in.FieldUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/field")
@RequiredArgsConstructor
@Validated
@Tag(name = "Canchas", description = "Operaciones relacionadas con las canchas de un complejo deportivo")
public class FieldController {

    private final FieldUseCase fieldUseCase;

    @Operation(summary = "Obtener las canchas activas de un complejo deportivo")
    @GetMapping("/p/{venueId}")
    public ResponseEntity<List<PublicFieldDTO>> getFieldsByVenueId(@PathVariable Long venueId) {
        return ResponseEntity.ok(fieldUseCase.findPublicByVenueId(venueId));
    }

    @Operation(summary = "Obtener las canchas de un complejo deportivo")
    @GetMapping
    public ResponseEntity<List<FieldDTO>> getAllFieldsByVenueId() {
        return ResponseEntity.ok(fieldUseCase.findAllByVenueId());
    }

    @Operation(summary = "Crear una nueva cancha")
    @PostMapping
    public ResponseEntity<FieldDTO> saveField(@Valid @RequestBody FieldRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldUseCase.save(request));
    }

    @Operation(summary = "Actualizar la información de una cancha")
    @PutMapping("/{id}")
    public ResponseEntity<FieldDTO> updateField(
            @PathVariable Long id,
            @Valid @RequestBody FieldRequest request) {
        return ResponseEntity.ok(fieldUseCase.update(request, id));
    }

    @Operation(summary = "Desactivar una cancha")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateById(@PathVariable Long id) {
        fieldUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar una cancha")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateById(@PathVariable Long id) {
        fieldUseCase.active(id);
        return ResponseEntity.noContent().build();
    }
}