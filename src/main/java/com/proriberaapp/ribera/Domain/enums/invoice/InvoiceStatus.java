package com.proriberaapp.ribera.Domain.enums.invoice;

import org.springframework.data.relational.core.sql.In;

public enum InvoiceStatus {
    PENDINGTOSEND("PENDIENTE DE ENVIAR"),
    CANCELLED("CANCELADO"),
    ACEPTED("ACEPTADO"),
    REJECTED("RECHAZADO");

    private String status;

    InvoiceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
