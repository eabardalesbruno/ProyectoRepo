package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AvailabilityResponse {
    private Boolean avaliable;
    private List<BookingConflictDto> conflictingBookings;
}
