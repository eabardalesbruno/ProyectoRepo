package com.proriberaapp.ribera.services.point;

public enum PointTransactionTypeEnum {
    TRANSFER("Transferencia"),
    REWARD("Recompensa"),
    REFUND("Reembolso"),
    EXCHANGE("Conversión"),
    CANJE("Canje");

    private String description;

    PointTransactionTypeEnum(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
