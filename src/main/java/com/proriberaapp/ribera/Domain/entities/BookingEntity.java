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
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("bookingstateid")
    private Integer bookingStateId;
    private BigDecimal costRelative;
    @Column("riberapoints")
    private Integer riberaPoints;
    @Column("inresortspoints")
    private Integer inResortsPoints;
    private Integer points;
    private String detail;
    private String amenities;
    private String services;
}