package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record RejectPaymentResponse(
        String nameOfBooker,
        String bookingType,
        String reasonForRejection,
        String solutionNote
) {
}
