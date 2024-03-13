package com.proriberaapp.ribera.Domain.enums;

public enum StateRoom {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    RESERVED("Reserved"),
    UNDER_MAINTENANCE("Under Maintenance"),
    OUT_OF_SERVICE("Out of Service");

    private final String description;

    StateRoom(String description) {
        this.description = description;
    }

}
