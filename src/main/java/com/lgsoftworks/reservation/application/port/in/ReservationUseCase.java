package com.lgsoftworks.reservation.application.port.in;

import com.lgsoftworks.reservation.application.dto.response.ReservationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationUseCase {
    Optional<ReservationDTO> findById(Long id);
    ReservationDTO findByCode(String code);
    List<ReservationDTO> findByFieldId(Long fieldId);
    List<ReservationDTO> findByVenueId(Long venueId);
    List<ReservationDTO> findByVenueIdAndDate(Long venueId, LocalDate date);
    ReservationDTO cancel(Long id);
    long countActiveByVenueIdAndDate(Long venueId, LocalDate date);
}
