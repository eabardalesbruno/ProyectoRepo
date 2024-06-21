package com.proriberaapp.ribera.Domain.entities;

import io.r2dbc.spi.Parameter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

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
    @Column("userclientid")
    private Integer userClientId;
    @Column("costfinal")
    private BigDecimal costFinal;
    private String detail;

    @Column("numberadults")
    private Integer numberAdults;
    @Column("numberchildren")
    private Integer numberChildren;
    @Column("numberbabies")
    private Integer numberBabies;

    @Column("daybookinginit")
    private Timestamp dayBookingInit;
    @Column("daybookingend")
    private Timestamp dayBookingEnd;
    @Column("checkin")
    private Timestamp checkIn;
    private Timestamp checkout;
    @Column("createdat")
    private Timestamp createdAt;
}