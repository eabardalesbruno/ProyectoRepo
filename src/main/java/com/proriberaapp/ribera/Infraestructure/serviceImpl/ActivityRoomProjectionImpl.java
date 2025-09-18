package com.proriberaapp.ribera.Infraestructure.serviceImpl;

import java.time.LocalDateTime;

import com.proriberaapp.ribera.Domain.Interfaces.ActivityRoomProjection;

import lombok.Data;

@Data
public class ActivityRoomProjectionImpl implements ActivityRoomProjection {
    private Integer roomId;
    private String roomNumber;
    private String roomName;
    private String roomTypeName;
    private String categoryName;
    private Integer bookingId;
    private LocalDateTime dayBookingInit;
    private LocalDateTime dayBookingEnd;
    private Integer numberAdults;
    private Integer numberChildren;
    private Integer numberBabies;
    private Integer numberAdultsExtra;
    private Integer numberAdultsMayor;
    private Integer bookingStateId;
    private String firstName;
    private String lastName;
    private Boolean isUserInClub;
    private String status;
    private Integer paymentStateId;
    private String paymentMethod;
    private Boolean hasFeeding;
}
