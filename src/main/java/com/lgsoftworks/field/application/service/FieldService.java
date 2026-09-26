package com.lgsoftworks.field.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.auth.domain.exception.AccessDeniedException;
import com.lgsoftworks.field.application.dto.mapper.FieldModelMapper;
import com.lgsoftworks.field.application.dto.request.FieldRequest;
import com.lgsoftworks.field.application.dto.response.FieldDTO;
import com.lgsoftworks.field.application.dto.response.PublicFieldDTO;
import com.lgsoftworks.field.application.port.in.FieldUseCase;
import com.lgsoftworks.field.domain.exception.FieldByIdNotFoundException;
import com.lgsoftworks.field.domain.model.Field;
import com.lgsoftworks.field.domain.port.out.FieldRepositoryPort;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import com.lgsoftworks.venue.application.port.in.VenueUseCase;
import com.lgsoftworks.venue.domain.exception.VenueByAdminIdNotFoundException;
import com.lgsoftworks.venue.domain.model.Venue;
import com.lgsoftworks.venue.domain.model.VenueId;
import com.lgsoftworks.venue.domain.port.out.VenueRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService implements FieldUseCase {

    private final FieldRepositoryPort fieldRepositoryPort;
    private final FieldModelMapper fieldModelMapper;
    private final VenueUseCase venueUseCase;

    @Override
    public FieldDTO save(FieldRequest request) {

        VenueDTO venue = venueUseCase.findByAdminId();

        Field field = Field.create(
                request.getName(),
                request.getFieldType(),
                request.getHourlyRate(),
                new VenueId(venue.getId())
        );
        Field saved = fieldRepositoryPort.save(field);
        return fieldModelMapper.toDTO(saved);
    }

    @Override
    public FieldDTO findById(Long id) {
        return fieldRepositoryPort.findById(id)
                .map(fieldModelMapper::toDTO)
                .orElseThrow(() -> new FieldByIdNotFoundException(id));
    }

    @Override
    public List<FieldDTO> findByVenueId() {

        VenueDTO venue = venueUseCase.findByAdminId();

        return fieldRepositoryPort.findByVenueId(venue.getId()).stream()
                .map(fieldModelMapper::toDTO)
                .toList();
    }

    @Override
    public List<FieldDTO> findAllByVenueId() {

        VenueDTO venue = venueUseCase.findByAdminId();

        return fieldRepositoryPort.findAllByVenueId(venue.getId()).stream()
                .map(fieldModelMapper::toDTO)
                .toList();
    }

    @Override
    public List<PublicFieldDTO> findPublicByVenueId(Long venueId) {
        return fieldRepositoryPort.findPublicByVenueId(venueId).stream()
                .map(fieldModelMapper::toPublicDTO)
                .toList();
    }

    @Override
    public FieldDTO update(FieldRequest request, Long fieldId) {

        Field field = fieldRepositoryPort.findById(fieldId)
                .orElseThrow(() -> new FieldByIdNotFoundException(fieldId));

        VenueDTO venue = venueUseCase.findByAdminId();

        if (!field.getVenueId().value().equals(venue.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar esta cancha");
        }

        field.updateFieldType(request.getFieldType());
        field.updatePrice(request.getHourlyRate());
        field.rename(request.getName());

        Field updated = fieldRepositoryPort.save(field);

        return fieldModelMapper.toDTO(updated);
    }

    @Override
    public void deactivate(Long fieldId) {
        Field field = fieldRepositoryPort.findById(fieldId)
                .orElseThrow(() -> new FieldByIdNotFoundException(fieldId));

        VenueDTO venue = venueUseCase.findByAdminId();

        if (!field.getVenueId().value().equals(venue.getId())) {
            throw new AccessDeniedException("No tienes permiso para desactivar esta cancha");
        }

        field.deactivate();
        fieldRepositoryPort.save(field);
    }

    @Override
    public void active(Long fieldId) {
        Field field = fieldRepositoryPort.findById(fieldId)
                .orElseThrow(() -> new FieldByIdNotFoundException(fieldId));

        VenueDTO venue = venueUseCase.findByAdminId();

        if (!field.getVenueId().value().equals(venue.getId())) {
            throw new AccessDeniedException("No tienes permiso para activar esta cancha");
        }

        field.activate();
        fieldRepositoryPort.save(field);
    }

}