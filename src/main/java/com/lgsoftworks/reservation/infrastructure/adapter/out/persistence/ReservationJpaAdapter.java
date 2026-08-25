package com.lgsoftworks.reservation.infrastructure.adapter.out.persistence;

import com.lgsoftworks.reservation.domain.model.Reservation;
import com.lgsoftworks.reservation.domain.model.ReservationStatus;
import com.lgsoftworks.reservation.domain.port.out.ReservationRepositoryPort;
import com.lgsoftworks.reservation.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.lgsoftworks.reservation.infrastructure.adapter.out.persistence.mapper.ReservationDboMapper;
import com.lgsoftworks.reservation.infrastructure.adapter.out.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationJpaAdapter implements ReservationRepositoryPort {

    private final ReservationRepository reservationRepository;
    private final ReservationDboMapper mapper;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity saved = reservationRepository.save(mapper.toDbo(reservation));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Reservation> findByCode(String code) {
        return reservationRepository.findByCode(code).map(mapper::toModel);
    }

    @Override
    public List<Reservation> findActiveByFieldId(Long fieldId) {
        return reservationRepository.findByFieldIdAndStatus(fieldId, ReservationStatus.ACTIVE)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Reservation> findActiveByVenueId(Long venueId) {
        return reservationRepository.findByField_Venue_IdAndStatus(venueId, ReservationStatus.ACTIVE)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Reservation> findActiveByFieldIdAndDate(Long fieldId, LocalDate date) {
        return reservationRepository.findByFieldIdAndReservationDateAndStatus(fieldId, date, ReservationStatus.ACTIVE)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Reservation> findActiveByVenueIdAndDate(Long venueId, LocalDate date) {
        return reservationRepository.findByField_Venue_IdAndStatusAndReservationDateOrderByStartTimeAsc(venueId, ReservationStatus.ACTIVE, date)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public long countActiveByVenueIdAndDate(Long venueId, LocalDate date) {
        return reservationRepository.countByField_Venue_IdAndStatusAndReservationDate(
                venueId, ReservationStatus.ACTIVE, date);
    }
}