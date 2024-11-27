package com.proriberaapp.ribera.Domain.dto;

import java.math.BigDecimal;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingAndRoomNameDto {
    @Column("bookingid")
    private Integer bookingId;
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("bookingstateid")
    private Integer bookingStateId;
    @Column("userclientid")
    private Integer userClientId;
    @Column("userpromotorid")
    private Integer userPromotorId;
    @Column("costfinal")
    private BigDecimal costFinal;
    @Column("numberadults")
    private Integer numberAdults;
    @Column("numberchildren")
    private Integer numberChildren;
    @Column("numberbabies")
    private Integer numberBabies;
    @Column("numberadultsextra")
    private Integer numberAdultsExtra;
    @Column("numberadultsmayor")
    private Integer numberAdultsMayor;
    @Column("roomname")
    private String roomName;
    @Column("roomdescription")
    private String roomDescription;
}
