package com.proriberaapp.ribera.Api.controllers.client.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class BookingSaveRequest {
    @Nullable
    private Integer bookingId;

    private Integer roomOfferId;

    @Nullable
    private Integer userClientId;

    @Nullable
    private Integer numberAdult;
    @Nullable
    private Integer numberChild;
    @Nullable
    private Integer numberBaby;
    @Nullable
    private Integer numberAdultExtra;
    @Nullable
    private Integer numberAdultMayor;
    @Nullable
    private BigDecimal infantCost;
    @Nullable
    private BigDecimal kidCost;
    @Nullable
    private BigDecimal adultCost;
    @Nullable
    private BigDecimal adultMayorCost;
    @Nullable
    private BigDecimal adultExtraCost;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayBookingInit;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayBookingEnd;

    @Nullable
    private List<FinalCostumer> finalCostumer;

    private List<Long> feedingIDs;

    private Integer totalCapacity;

    private boolean isPaying;

    private BigDecimal totalCost;

    private Integer quotationId;
}
