package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Nullable
    private List<FinalCostumer> finalCostumer;

}
