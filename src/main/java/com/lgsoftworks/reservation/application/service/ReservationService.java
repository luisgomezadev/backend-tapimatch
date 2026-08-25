package com.lgsoftworks.reservation.application.service;

import com.lgsoftworks.field.application.dto.response.FieldDTO;
import com.lgsoftworks.field.application.port.in.FieldUseCase;
import com.lgsoftworks.reservation.application.dto.mapper.ReservationModelMapper;
import com.lgsoftworks.reservation.application.dto.response.ReservationDTO;
import com.lgsoftworks.reservation.application.port.in.ReservationUseCase;
import com.lgsoftworks.reservation.domain.exception.ReservationByCodeNotFoundException;
import com.lgsoftworks.reservation.domain.exception.ReservationByIdNotFoundException;
import com.lgsoftworks.reservation.domain.model.Reservation;
import com.lgsoftworks.reservation.domain.port.out.ReservationRepositoryPort;
import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import com.lgsoftworks.venue.application.port.in.VenueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final ReservationModelMapper reservationModelMapper;
    private final FieldUseCase fieldUseCase;
    private final VenueUseCase venueUseCase;

    @Override
    public Optional<ReservationDTO> findById(Long id) {
        return reservationRepositoryPort.findById(id).map(reservationModelMapper::toDTO);
    }

    @Override
    public Optional<ReservationDTO> findByCode(String code) {
        Optional<ReservationDTO> optionalReservationDTO = reservationRepositoryPort.findByCode(code)
                .map(reservationModelMapper::toDTO);

        if (optionalReservationDTO.isPresent()) {
            FieldDTO fieldDTO = fieldUseCase.findById(optionalReservationDTO.get().getFieldId());
            VenueDTO venueDTO = venueUseCase.findById(fieldDTO.getVenueId());

            optionalReservationDTO.get().setVenueName(venueDTO.getName());
            optionalReservationDTO.get().setFieldName(fieldDTO.getName());

            return optionalReservationDTO;
        } else {
            throw new ReservationByCodeNotFoundException(code);
        }

    }

    @Override
    public List<ReservationDTO> findByFieldId(Long fieldId) {
        return reservationRepositoryPort.findActiveByFieldId(fieldId).stream()
                .map(reservationModelMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findByVenueId(Long venueId) {
        return reservationRepositoryPort.findActiveByVenueId(venueId).stream()
                .map(reservationModelMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findByVenueIdAndDate(Long venueId, LocalDate date) {
        List<Reservation> reservations = reservationRepositoryPort.findActiveByVenueIdAndDate(venueId, date);

        Map<Long, FieldDTO> fieldsById = fieldUseCase.findByVenueId(venueId).stream()
                .collect(Collectors.toMap(FieldDTO::getId, Function.identity()));

        return reservations.stream()
                .map(r -> {
                    ReservationDTO dto = reservationModelMapper.toDTO(r);
                    FieldDTO field = fieldsById.get(r.getFieldId());
                    if (field != null) {
                        dto.setFieldName(field.getName());
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    public ReservationDTO cancel(Long id) {
        Reservation reservation = reservationRepositoryPort.findById(id)
                .orElseThrow(() -> new ReservationByIdNotFoundException(id));
        reservation.cancel();
        Reservation saved = reservationRepositoryPort.save(reservation);
        return reservationModelMapper.toDTO(saved);
    }

    @Override
    public long countActiveByVenueIdAndDate(Long venueId, LocalDate date) {
        return reservationRepositoryPort.countActiveByVenueIdAndDate(venueId, date);
    }
}