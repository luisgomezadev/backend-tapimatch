package com.lgsoftworks.field.infrastructure.adapter.out.persistence;

import com.lgsoftworks.field.infrastructure.adapter.out.persistence.repository.FieldRepository;
import com.lgsoftworks.venue.domain.port.out.FieldAvailabilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FieldAvailabilityAdapter implements FieldAvailabilityPort {

    private final FieldRepository fieldRepository;

    @Override
    public boolean existsFieldsForVenue(Long venueId) {
        return fieldRepository.existsByVenueId(venueId);
    }

    @Override
    public Set<Long> venueIdsWithFields(Collection<Long> venueIds) {
        if (venueIds == null || venueIds.isEmpty()) {
            return Set.of();
        }
        return fieldRepository.findVenueIdsWithFields(venueIds);
    }
}