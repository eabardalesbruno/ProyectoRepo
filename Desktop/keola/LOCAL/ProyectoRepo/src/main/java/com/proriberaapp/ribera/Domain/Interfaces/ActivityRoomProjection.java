package com.proriberaapp.ribera.Domain.Interfaces;

import java.time.LocalDateTime;

public interface ActivityRoomProjection {
    Integer getRoomId();

    String getRoomNumber();

    String getRoomName();

    String getRoomTypeName();

    String getCategoryName();

    Integer getBookingId();

    LocalDateTime getDayBookingInit();

    LocalDateTime getDayBookingEnd();

    Integer getNumberAdults();

    Integer getNumberChildren();

    Integer getNumberBabies();

    Integer getNumberAdultsExtra();

    Integer getNumberAdultsMayor();

    Integer getBookingStateId();

    String getFirstName();

    String getLastName();

    Boolean getIsUserInClub();

    String getStatus();

    Integer getPaymentStateId();

    String getPaymentMethod();

    Boolean getHasFeeding();
}
