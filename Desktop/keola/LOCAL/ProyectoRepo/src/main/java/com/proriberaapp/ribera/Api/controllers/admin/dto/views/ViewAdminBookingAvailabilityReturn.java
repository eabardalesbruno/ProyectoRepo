package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import io.r2dbc.spi.Readable;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ViewAdminBookingAvailabilityReturn(
        Integer roomId,
        Integer roomOfferId,
        String numberRoom,
        String checkIn, // 11/01/2024 11:46
        String checkOut, // 11/01/2024 11:46
        String client,
        String type,
        String nameRoom,
        Map<Integer, String> numberPerson, // 02 adultos, 02 niños, 01 infantes
        String state // Ocupado parcial, Ocupado total, Reservado parcial, Reservado total, libre
) {
    public static ViewAdminBookingAvailabilityReturn convertTo(Readable row) {
        return new ViewAdminBookingAvailabilityReturn(
                row.get("roomid", Integer.class),
                row.get("roomofferid", Integer.class),
                row.get("numberRoom", String.class),
                row.get("checkIn", String.class),
                row.get("checkOut", String.class),
                row.get("client", String.class),
                row.get("type", String.class),
                row.get("nameRoom", String.class),
                Map.of(1, "02 adultos", 2, "02 niños", 3, "01 infantes"),
                row.get("state", String.class)
        );
    }
}
