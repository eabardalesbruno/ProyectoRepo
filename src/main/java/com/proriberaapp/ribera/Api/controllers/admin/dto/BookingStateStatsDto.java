package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingStateStatsDto {

    String bookingstate;
    Integer total;
    Double percentage;

}
