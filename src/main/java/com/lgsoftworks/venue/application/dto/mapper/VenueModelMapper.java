package com.lgsoftworks.venue.application.dto.mapper;

import com.lgsoftworks.venue.application.dto.response.VenueDTO;
import com.lgsoftworks.venue.domain.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VenueModelMapper {

    @Mapping(target = "hasFields", ignore = true)
    VenueDTO toDTO(Venue venue);

}