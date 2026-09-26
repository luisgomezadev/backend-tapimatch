package com.lgsoftworks.field.application.dto.mapper;

import com.lgsoftworks.field.application.dto.response.FieldDTO;
import com.lgsoftworks.field.application.dto.response.PublicFieldDTO;
import com.lgsoftworks.field.domain.model.Field;
import com.lgsoftworks.venue.domain.model.VenueId;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FieldModelMapper {

    FieldDTO toDTO(Field field);

    PublicFieldDTO toPublicDTO(Field field);

    default Long map(VenueId venueId) {
        return venueId == null ? null : venueId.value();
    }

}