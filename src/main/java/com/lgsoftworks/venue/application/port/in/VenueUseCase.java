package com.lgsoftworks.venue.application.port.in;

import com.lgsoftworks.common.response.PageResponse;
import com.lgsoftworks.venue.application.dto.request.VenueFilter;
import com.lgsoftworks.venue.application.dto.request.VenueRequest;
import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VenueUseCase {
    VenueDTO save(VenueRequest request);
    VenueDTO update(Long id, VenueRequest request);
    VenueDTO findById(Long id);
    VenueDTO findByCode(String code);
    VenueDTO findByAdminId();
    PageResponse<VenueDTO> searchVenues(VenueFilter filter, Pageable pageable);
}