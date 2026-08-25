package com.lgsoftworks.venue.domain.port.out;

import java.util.Collection;
import java.util.Set;

public interface FieldAvailabilityPort {
    boolean existsFieldsForVenue(Long venueId);
    Set<Long> venueIdsWithFields(Collection<Long> venueIds);
}
