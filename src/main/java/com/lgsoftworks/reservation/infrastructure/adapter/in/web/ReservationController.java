package com.lgsoftworks.reservation.infrastructure.adapter.in.web;

import com.lgsoftworks.reservation.application.dto.request.ReservationRequest;
import com.lgsoftworks.reservation.application.dto.response.ReservationDTO;
import com.lgsoftworks.reservation.application.dto.response.TimeSlot;
import com.lgsoftworks.reservation.application.port.in.ReservationAvailabilityUseCase;
import com.lgsoftworks.reservation.application.port.in.ReservationUseCase;
import com.lgsoftworks.reservation.domain.exception.ReservationByCodeNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reservaciones", description = "Operaciones relacionadas con las reservaciones")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;
    private final ReservationAvailabilityUseCase reservationAvailabilityUseCase;

    @Operation(
            summary = "Obtener reservas por ID de la cancha",
            description = "Devuelve una lista de reservas activas asociadas a una cancha específica, identificada por su ID.",
            parameters = {
                    @Parameter(name = "fieldId", description = "ID de la cancha para la que se desean obtener las reservas",
                            required = true, example = "1")
            }
    )
    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByFieldId(@PathVariable Long fieldId) {
        return ResponseEntity.ok(reservationUseCase.findByFieldId(fieldId));
    }

    @Operation(
            summary = "Obtener reservas por ID del complejo",
            description = "Devuelve una lista de reservas activas asociadas a un complejo específico, identificado por su ID.",
            parameters = {
                    @Parameter(name = "venueId", description = "ID del complejo para el que se desean obtener las reservas",
                            required = true, example = "1")
            }
    )
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByVenueId(@PathVariable Long venueId) {
        return ResponseEntity.ok(reservationUseCase.findByVenueId(venueId));
    }

    @Operation(
            summary = "Obtener reserva por código",
            description = "Devuelve una reserva específica, identificada por su código.",
            parameters = {
                    @Parameter(name = "code", description = "Código de la reserva para obtener toda su información",
                            required = true, example = "ABC123")
            }
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<ReservationDTO> getReservationByCode(@PathVariable String code) {
        return reservationUseCase.findByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ReservationByCodeNotFoundException(code));
    }

    @Operation(
            summary = "Obtener reservas por ID del complejo y fecha",
            description = "Devuelve una lista de reservas activas asociadas a un complejo y una fecha específica.",
            parameters = {
                    @Parameter(name = "venueId", description = "ID del complejo para el que se desean obtener las reservas",
                            required = true, example = "1")
            }
    )
    @GetMapping("/venue/{venueId}/date")
    public ResponseEntity<List<ReservationDTO>> getReservationsByVenueIdAndDate(
            @PathVariable Long venueId,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reservationUseCase.findByVenueIdAndDate(venueId, date));
    }

    @Operation(
            summary = "Crear una nueva reserva",
            description = "Valida disponibilidad y horario del complejo, genera un código único, y guarda la reserva."
    )
    @PostMapping
    public ResponseEntity<ReservationDTO> saveReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationAvailabilityUseCase.reservationAvailability(reservationRequest));
    }

    @Operation(
            summary = "Cancelar una reserva",
            description = "Cancela una reserva activa usando su ID. Solo se puede cancelar una reserva que esté activa.",
            parameters = {
                    @Parameter(name = "id", description = "ID de la reserva que se desea cancelar", required = true, example = "12")
            }
    )
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationUseCase.cancel(id));
    }

    @Operation(
            summary = "Consultar horarios disponibles",
            description = "Devuelve los espacios de tiempo disponibles para una cancha, complejo y fecha específicos."
    )
    @GetMapping("/availability/hours")
    public ResponseEntity<List<TimeSlot>> getHoursAvailability(
            @RequestParam Long venueId,
            @RequestParam Long fieldId,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reservationAvailabilityUseCase.getAvailableSlots(venueId, fieldId, date));
    }

    @Operation(
            summary = "Contar reservas activas por complejo y fecha",
            description = "Devuelve el numero de reservas activas asociadas a un complejo en una fecha especifica.",
            parameters = {
                    @Parameter(name = "venueId", description = "ID del complejo para el que se desea contar las reservas",
                            required = true, example = "1")
            }
    )
    @GetMapping("/venue/{venueId}/date/count")
    public ResponseEntity<Long> countReservationsByVenueIdAndDate(
            @PathVariable Long venueId,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reservationUseCase.countActiveByVenueIdAndDate(venueId, date));
    }
}