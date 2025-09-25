package com.proriberaapp.ribera.utils;

public class RoomOfferUtil {
    public static Float safeGetValue(Float value, Float defaultValue) {
        return value != null ? value : defaultValue;
    }
}
