package com.lgsoftworks.reservation.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.reservation.domain.model.ReservationStatus;
import com.lgsoftworks.reservation.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long>, JpaSpecificationExecutor<ReservationEntity> {
    Optional<ReservationEntity> findByCode(String code);

    List<ReservationEntity> findByFieldIdAndStatus(Long fieldId, ReservationStatus status);

    List<ReservationEntity> findByFieldIdAndReservationDateAndStatus(
            Long fieldId, LocalDate date, ReservationStatus status);

    List<ReservationEntity> findByField_Venue_IdAndStatus(Long venueId, ReservationStatus status);

    List<ReservationEntity> findByField_Venue_IdAndStatusAndReservationDateOrderByStartTimeAsc(
            Long venueId, ReservationStatus status, LocalDate date);

    long countByField_Venue_IdAndStatusAndReservationDate(
            Long venueId, ReservationStatus status, LocalDate date);
}
