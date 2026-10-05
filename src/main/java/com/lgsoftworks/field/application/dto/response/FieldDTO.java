package com.lgsoftworks.field.application.dto.response;

import com.lgsoftworks.field.domain.model.FieldType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class FieldDTO {
    private Long id;
    private String name;
    private FieldType fieldType;
    private BigDecimal hourlyRate;
    private Long venueId;
    private boolean active;
}