package com.lgsoftworks.field.application.port.in;

import com.lgsoftworks.field.application.dto.request.FieldRequest;
import com.lgsoftworks.field.application.dto.response.FieldDTO;
import com.lgsoftworks.field.application.dto.response.PublicFieldDTO;
import com.lgsoftworks.field.domain.model.Field;

import java.math.BigDecimal;
import java.util.List;

public interface FieldUseCase {
    FieldDTO save(FieldRequest request);
    FieldDTO findById(Long id);
    List<FieldDTO> findByVenueId();
    List<FieldDTO> findAllByVenueId();
    List<PublicFieldDTO> findPublicByVenueId(Long venueId);
    FieldDTO update(FieldRequest request, Long fieldId);
    void deactivate(Long fieldId);
    void active(Long fieldId);
}