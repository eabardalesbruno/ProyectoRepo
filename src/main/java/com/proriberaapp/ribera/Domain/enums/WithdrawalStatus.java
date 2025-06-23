package com.proriberaapp.ribera.Domain.enums;

public enum WithdrawalStatus {
    PENDING("Pendiente"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    PROCESSED("Procesado");

    private final String description;

    WithdrawalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 