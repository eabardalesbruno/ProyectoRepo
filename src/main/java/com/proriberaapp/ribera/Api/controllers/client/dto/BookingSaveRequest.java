package com.proriberaapp.ribera.Api.controllers.client.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookingSaveRequest {

    private Integer roomOfferId;

    @Nullable
    private Integer numberAdult;
    @Nullable
    private Integer numberChild;
    @Nullable
    private Integer numberBaby;

    private LocalDate dayBookingInit;
    private LocalDate dayBookingEnd;

}
