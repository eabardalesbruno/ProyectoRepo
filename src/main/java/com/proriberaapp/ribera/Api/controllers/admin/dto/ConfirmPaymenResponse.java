package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record ConfirmPaymenResponse(
        String nameOfBooker,
        String bookingType,
        String checkInDate,
        String checkOutDate,
        Integer totalLengthStay,
        String selectedBooking
) {
}
