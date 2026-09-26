package com.lgsoftworks.venue.application.service;

import com.lgsoftworks.auth.application.service.CurrentUserService;
import com.lgsoftworks.auth.domain.exception.AccessDeniedException;
import com.lgsoftworks.common.response.PageResponse;
import com.lgsoftworks.user.domain.model.User;
import com.lgsoftworks.venue.application.dto.mapper.VenueModelMapper;
import com.lgsoftworks.venue.application.dto.request.VenueFilter;
import com.lgsoftworks.venue.application.dto.request.VenueRequest;
import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import com.lgsoftworks.venue.application.port.in.VenueUseCase;
import com.lgsoftworks.venue.domain.exception.VenueByAdminIdNotFoundException;
import com.lgsoftworks.venue.domain.exception.VenueByCodeNotFoundException;
import com.lgsoftworks.venue.domain.exception.VenueByIdNotFoundException;
import com.lgsoftworks.venue.domain.model.Venue;
import com.lgsoftworks.venue.domain.port.out.FieldAvailabilityPort;
import com.lgsoftworks.venue.domain.port.out.VenueRepositoryPort;
import com.lgsoftworks.venue.domain.service.VenueUniquenessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;
    private final VenueUniquenessValidator venueUniquenessValidator;
    private final VenueModelMapper venueModelMapper;
    private final CurrentUserService currentUserService;
    private final FieldAvailabilityPort fieldAvailabilityPort;

    @Override
    public VenueDTO save(VenueRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        venueUniquenessValidator.validate(request.getCode());

        Venue venue = Venue.create(
                request.getCode(),
                request.getName(),
                request.getCity(),
                request.getAddress(),
                request.getOpeningHour(),
                request.getClosingHour(),
                currentUser.getId()
        );
        Venue saved = venueRepositoryPort.save(venue);

        VenueDTO dto = venueModelMapper.toDTO(saved);
        dto.setHasFields(false);
        return dto;
    }

    @Override
    public VenueDTO update(Long id, VenueRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Venue venue = venueRepositoryPort.findById(id)
                .orElseThrow(() -> new VenueByIdNotFoundException(id));

        if (!venue.getAdminId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar este complejo deportivo");
        }

        venueUniquenessValidator.validateForUpdate(request.getCode(), id);

        venue.rename(request.getName());
        venue.changeCode(request.getCode());
        venue.changeAddress(request.getCity(), request.getAddress());
        venue.changeSchedule(request.getOpeningHour(), request.getClosingHour());

        Venue saved = venueRepositoryPort.save(venue);
        return toDtoWithFields(saved);
    }

    @Override
    public VenueDTO findById(Long id) {
        Venue venue = venueRepositoryPort.findById(id)
                .orElseThrow(() -> new VenueByIdNotFoundException(id));
        return toDtoWithFields(venue);
    }

    @Override
    public VenueDTO findByCode(String code) {
        Venue venue = venueRepositoryPort.findByCode(code)
                .orElseThrow(() -> new VenueByCodeNotFoundException(code));
        return toDtoWithFields(venue);
    }

    @Override
    public VenueDTO findByAdminId() {
        User currentUser = currentUserService.getCurrentUser();
        return venueRepositoryPort.findByAdminId(currentUser.getId())
                .map(this::toDtoWithFields)
                .orElseThrow(() -> new VenueByAdminIdNotFoundException(currentUser.getId()));
    }

    @Override
    public PageResponse<VenueDTO> searchVenues(VenueFilter filter, Pageable pageable) {
        Page<Venue> venuesPage = venueRepositoryPort.search(filter.name(), filter.city(), pageable);

        List<Long> venueIds = venuesPage.getContent().stream()
                .map(Venue::getId)
                .collect(Collectors.toList());
        Set<Long> idsWithFields = fieldAvailabilityPort.venueIdsWithFields(venueIds);

        Page<VenueDTO> dtoPage = venuesPage.map(venue -> {
            VenueDTO dto = venueModelMapper.toDTO(venue);
            dto.setHasFields(idsWithFields.contains(venue.getId()));
            return dto;
        });

        return PageResponse.from(dtoPage);
    }

    private VenueDTO toDtoWithFields(Venue venue) {
        VenueDTO dto = venueModelMapper.toDTO(venue);
        dto.setHasFields(fieldAvailabilityPort.existsFieldsForVenue(venue.getId()));
        return dto;
    }
}