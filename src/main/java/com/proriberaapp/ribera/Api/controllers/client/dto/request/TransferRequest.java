package com.proriberaapp.ribera.Api.controllers.client.dto.request;

public record TransferRequest(
        String fromInput,
        String toInput,
        Double amount,
        String subCategory,
        String passwordConfirm
) {}