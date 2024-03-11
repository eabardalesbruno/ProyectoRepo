package com.proriberaapp.ribera.Domain.entities;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Table("bookingdetail")
public class BookingDetailEntity {
    private Integer bookingDetailId;
    private Integer roomId;
    private Integer bookingId;
    private Integer paymentStateId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String roomNumber;
    private String roomType;
    private String roomName;
    private Integer capacity;
    private String customer;
    private String documentType;
    private String documentNumber;
    private Integer adultsNumber;
    private Integer childrenNumber;
    private Integer babiesNumber;
}