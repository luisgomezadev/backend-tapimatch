package com.lgsoftworks.reservation.domain.port.out;

import com.lgsoftworks.reservation.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    Optional<Reservation> findByCode(String code);
    List<Reservation> findActiveByFieldId(Long fieldId);
    List<Reservation> findActiveByVenueId(Long venueId);
    List<Reservation> findActiveByFieldIdAndDate(Long fieldId, LocalDate date);
    List<Reservation> findActiveByVenueIdAndDate(Long venueId, LocalDate date);
    long countActiveByVenueIdAndDate(Long venueId, LocalDate date);
}