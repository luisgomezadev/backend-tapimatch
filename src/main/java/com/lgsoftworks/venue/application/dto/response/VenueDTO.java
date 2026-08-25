package com.lgsoftworks.venue.application.dto.response;

import com.lgsoftworks.shared.domain.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class VenueDTO {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String address;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private boolean hasFields;
}