package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@Table("booking")
public class BookingEntity {
    private Integer bookingId;
    private Integer userId;
    private Integer paymentMethodId;
    private Integer bookingStateId;
    private String roomType;
    private String roomName;
    private Integer capacity;
    private BigDecimal cost;
    private Integer riberaPoints;
    private Integer inResortsPoints;
    private String detail;
    private String amenities;
    private String services;
    private String image;
}