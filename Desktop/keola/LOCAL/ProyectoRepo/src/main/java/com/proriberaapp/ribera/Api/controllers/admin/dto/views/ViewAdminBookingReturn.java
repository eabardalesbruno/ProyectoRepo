package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import io.r2dbc.spi.Readable;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ViewAdminBookingReturn(
        Integer roomId,
        Integer roomOfferId,
        Integer roomTypeId,
        Integer bookingId,
        Integer bookingStateId,
        Integer userClientId,

        LocalDateTime dateCreate, // 11/01/2024
        String dateCreateString, // 11/01/2024 11:46
        String numberBooking,

        LocalDateTime dayBookingInit, // 11/01/2024 11:46
        String dayBookingInitString, // 11/01/2024 11:46
        LocalDateTime dayBookingEnd, // 11/01/2024 11:46
        String dayBookingEndString, // 11/01/2024 11:46

        LocalDateTime checkIn, // 11/01/2024 11:46
        String checkInString, // 11/01/2024 11:46
        LocalDateTime checkOut, // 11/01/2024 11:46
        String checkOutString, // 11/01/2024 11:46

        String numberRoom,
        String typeRoom,
        String nameRoom,
        Integer capacity,
        String client,
        String typeDocument,
        String numberDocument,

        BigDecimal costTotal,
        String costTotalString,

        String numberAdult, //TODO falta obtener esos datos
        String numberChild, //TODO falta obtener esos datos
        String numberBaby //TODO falta obtener esos datos
) {
    public static ViewAdminBookingReturn convertTo(Readable row) {
        return ViewAdminBookingReturn.builder()
                .roomId(row.get("roomid", Integer.class))
                .roomOfferId(row.get("roomofferid", Integer.class))
                .roomTypeId(row.get("roomtypeid", Integer.class))
                .bookingId(row.get("bookingid", Integer.class))
                .bookingStateId(row.get("bookingstateid", Integer.class))
                .userClientId(row.get("userclientid", Integer.class))

                .dateCreate(row.get("dateCreate", LocalDateTime.class))
                .dateCreateString(row.get("dateCreateString", String.class))
                .numberBooking(row.get("numberBooking", String.class))

                .dayBookingInit(row.get("dayBookingInit", LocalDateTime.class))
                .dayBookingInitString(row.get("dayBookingInitString", String.class))
                .dayBookingEnd(row.get("dayBookingEnd", LocalDateTime.class))
                .dayBookingEndString(row.get("dayBookingEndString", String.class))

                .checkIn(row.get("checkIn", LocalDateTime.class))
                .checkInString(row.get("checkInString", String.class))
                .checkOut(row.get("checkOut", LocalDateTime.class))
                .checkOutString(row.get("checkOutString", String.class))

                .numberRoom(row.get("numberRoom", String.class))
                .typeRoom(row.get("typeRoom", String.class))
                .nameRoom(row.get("nameRoom", String.class))
                .capacity(row.get("capacity", Integer.class))
                .client(row.get("client", String.class))
                .typeDocument(row.get("typeDocument", String.class))
                .numberDocument(row.get("numberDocument", String.class))

                .costTotal(row.get("costTotal", BigDecimal.class))
                .costTotalString(row.get("costTotalString", String.class))

                .numberAdult(row.get("numberAdult", String.class))
                .numberChild(row.get("numberChild", String.class))
                .numberBaby(row.get("numberBaby", String.class))
                .build();
    }
}
