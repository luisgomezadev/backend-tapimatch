package com.lgsoftworks.field.domain.model;

import com.lgsoftworks.field.domain.exception.InvalidFieldDataException;
import com.lgsoftworks.venue.domain.model.VenueId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Field {
    private Long id;
    private String name;
    private FieldType fieldType;
    private BigDecimal hourlyRate;
    private VenueId venueId;
    private boolean active;

    public static Field create(String name, FieldType fieldType, BigDecimal hourlyRate, VenueId venueId) {
        Field field = new Field();
        field.name = requireNonBlank(name);
        field.fieldType = Objects.requireNonNull(fieldType, "El tipo de cancha es requerido");
        field.hourlyRate = requirePositive(hourlyRate);
        field.venueId = Objects.requireNonNull(venueId, "La cancha debe pertenecer a un complejo");
        field.active = true;
        return field;
    }

    public static Field rehydrate(Long id, String name, FieldType fieldType, BigDecimal hourlyRate,
                                  VenueId venueId, boolean active) {
        Field field = new Field();
        field.id = id;
        field.name = name;
        field.fieldType = fieldType;
        field.hourlyRate = hourlyRate;
        field.venueId = venueId;
        field.active = active;
        return field;
    }

    public void updatePrice(BigDecimal newHourlyRate) {
        this.hourlyRate = requirePositive(newHourlyRate);
    }

    public void rename(String newName) {
        this.name = requireNonBlank(newName);
    }

    public void updateFieldType(FieldType newFieldType) {
        if (newFieldType == null) {
            throw new InvalidFieldDataException("El tipo de la cancha es requerido");
        }
        this.fieldType = newFieldType;
    }

    public void deactivate() {
        if (!this.active) {
            throw new InvalidFieldDataException("La cancha ya está inactiva");
        }
        this.active = false;
    }

    public void activate() {
        if (this.active) {
            throw new InvalidFieldDataException("La cancha ya está inactiva");
        }
        this.active = true;
    }

    private static String requireNonBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidFieldDataException("El nombre de la cancha es requerido");
        }
        return value;
    }

    private static BigDecimal requirePositive(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFieldDataException("El precio por hora debe ser positivo");
        }
        return value;
    }

}