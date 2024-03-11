package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@Table("booking")
public class BookingEntity {
    @Id
    @Column("bookingid")
    private Integer bookingId;
    @Column("userid")
    private Integer userId;
    @Column("paymentmethodid")
    private Integer paymentMethodId;
    @Column("paymentstateid")
    private Integer bookingStateId;
    @Column("roomtype")
    private String roomType;
    @Column("roomname")
    private String roomName;
    private Integer capacity;
    private BigDecimal cost;
    @Column("riberapoints")
    private Integer riberaPoints;
    @Column("inresortspoints")
    private Integer inResortsPoints;
    private String detail;
    private String amenities;
    private String services;
    private String image;
}