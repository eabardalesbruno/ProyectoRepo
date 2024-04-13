package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Table("bookingdetail")
public class BookingDetailEntity {
    @Id
    @Column("bookingdetailid")
    private Integer bookingDetailId;
    @Column("roomid")
    private Integer roomId;
    @Column("bookingid")
    private Integer bookingId;
    @Column("paymentstateid")
    private Integer paymentStateId;
    @Column("checkin")
    private LocalDateTime checkIn;
    @Column("checkout")
    private LocalDateTime checkOut;
    @Column("roomnumber")
    private String roomNumber;
    @Column("roomtype")
    private String roomType;
    @Column("roomname")
    private String roomName;
    private Integer capacity;
    private String customer;
    @Column("documenttype")
    private String documentType;
    @Column("documentnumber")
    private String documentNumber;
    @Column("adultsnumber")
    private Integer adultsNumber;
    @Column("childrennumber")
    private Integer childrenNumber;
    @Column("babiesnumber")
    private Integer babiesNumber;
}