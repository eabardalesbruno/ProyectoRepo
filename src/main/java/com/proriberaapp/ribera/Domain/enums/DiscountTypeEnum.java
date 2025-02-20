package com.proriberaapp.ribera.Domain.enums;

public enum DiscountTypeEnum {
    POINTS("Puntos");

    private String description;

    DiscountTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
