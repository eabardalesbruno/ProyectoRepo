package com.proriberaapp.ribera.Api.controllers.admin.dto.bookingcancellations.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingCancellationRequest {
    private Integer bookingId;
    private Integer userClientId;
    private String cancellationReason;
    private BigDecimal additionalCost;
    private Integer receptionistId;
}
