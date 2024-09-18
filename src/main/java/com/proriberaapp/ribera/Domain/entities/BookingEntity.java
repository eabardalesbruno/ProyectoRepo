package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

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

    public static BookingEntity createBookingEntity(Integer userClientId, BookingSaveRequest bookingSaveRequest, Integer numberOfDays) {
        return BookingEntity.builder()
                .roomOfferId(bookingSaveRequest.getRoomOfferId())
                .bookingStateId(3)
                .userClientId(userClientId)
                .numberAdults(bookingSaveRequest.getNumberAdult())
                .numberChildren(bookingSaveRequest.getNumberChild())
                .numberBabies(bookingSaveRequest.getNumberBaby())
                .dayBookingInit(Timestamp.valueOf(bookingSaveRequest.getDayBookingInit().atStartOfDay()))
                .dayBookingEnd(Timestamp.valueOf(bookingSaveRequest.getDayBookingEnd().atStartOfDay()))
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public boolean hasPassed30Minutes() {
        LocalDateTime createdTime = createdAt.toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, currentTime);
        return duration.toMinutes() >= 30;
    }
}