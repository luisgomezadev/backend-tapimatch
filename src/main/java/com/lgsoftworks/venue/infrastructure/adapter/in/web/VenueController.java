package com.lgsoftworks.venue.infrastructure.adapter.in.web;

import com.lgsoftworks.common.response.PageResponse;
import com.lgsoftworks.venue.application.dto.request.CreateVenueWithFieldsRequest;
import com.lgsoftworks.venue.application.dto.request.VenueFilter;
import com.lgsoftworks.venue.application.dto.request.VenueRequest;
import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import com.lgsoftworks.venue.application.dto.response.VenueWithFieldsDTO;
import com.lgsoftworks.venue.application.port.in.CreateVenueWithFieldsUseCase;
import com.lgsoftworks.venue.application.port.in.VenueUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/venue")
@RequiredArgsConstructor
@Validated
@Tag(name = "Complejos", description = "Operaciones relacionadas con los complejos deportivos")
public class VenueController {

    private final VenueUseCase venueUseCase;
    private final CreateVenueWithFieldsUseCase createVenueWithFieldsUseCase;

    @Operation(summary = "Obtener los complejos deportivos paginados")
    @GetMapping
    public ResponseEntity<PageResponse<VenueDTO>> getVenues(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        VenueFilter filter = new VenueFilter(name, city);
        return ResponseEntity.ok(venueUseCase.searchVenues(filter, PageRequest.of(page, size)));
    }

    @Operation(summary = "Obtener un complejo por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Complejo encontrado"),
            @ApiResponse(responseCode = "404", description = "Complejo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueUseCase.findById(id));
    }

    @Operation(summary = "Obtener un complejo por ID del admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Complejo encontrado"),
            @ApiResponse(responseCode = "404", description = "Complejo no encontrado")
    })
    @GetMapping("/mine")
    public ResponseEntity<VenueDTO> getMyVenue() {
        return ResponseEntity.ok(venueUseCase.findByAdminId());
    }

    @Operation(summary = "Obtener un complejo por su código (url)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Complejo encontrado"),
            @ApiResponse(responseCode = "404", description = "Complejo no encontrado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<VenueDTO> getVenueByCode(@PathVariable String code) {
        return ResponseEntity.ok(venueUseCase.findByCode(code));
    }

    @Operation(summary = "Crear un nuevo complejo deportivo")
    @PostMapping
    public ResponseEntity<VenueDTO> saveVenue(@Valid @RequestBody VenueRequest venueRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueUseCase.save(venueRequest));
    }

    @Operation(summary = "Actualizar un complejo deportivo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Complejo actualizado"),
            @ApiResponse(responseCode = "404", description = "Complejo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueRequest venueRequest) {
        return ResponseEntity.ok(venueUseCase.update(id, venueRequest));
    }

    @Operation(
            summary = "Crear un complejo deportivo junto con sus canchas",
            description = "Crea el complejo y todas sus canchas en una sola operación transaccional."
    )
    @PostMapping("/with-fields")
    public ResponseEntity<VenueWithFieldsDTO> saveVenueWithFields(
            @Valid @RequestBody CreateVenueWithFieldsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createVenueWithFieldsUseCase.execute(request));
    }
}