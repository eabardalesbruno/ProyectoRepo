package com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingRoomChangesRequest {
    private Integer bookingId;
    private Integer userClientId;
    private String changeReason;
    private Integer oldRoomOfferId;
    private Integer newRoomOfferId;
    private BigDecimal additionalCost;
    private String checkIn;
    private String checkOut;
    private String additionalService;
    private Integer receptionistId;
}
