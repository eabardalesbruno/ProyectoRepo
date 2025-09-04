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
import java.time.*;

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
    @Column("userpromotorid")
    private Integer userPromotorId;
    @Column("receptionistid")
    private Integer receptionistId;
    @Column("costfinal")
    private BigDecimal costFinal;
    private String detail;

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

    @Column("infantcost")
    private BigDecimal infantCost;
    @Column("kidcost")
    private BigDecimal kidCost;
    @Column("adultcost")
    private BigDecimal adultCost;
    @Column("adultmayorcost")
    private BigDecimal adultMayorCost;
    @Column("adultextracost")
    private BigDecimal adultExtraCost;

    @Column("daybookinginit")
    private Timestamp dayBookingInit;
    @Column("daybookingend")
    private Timestamp dayBookingEnd;
    @Column("checkin")
    private Timestamp checkIn;
    private Timestamp checkout;

    @Column("total_rewards")
    private Integer totalRewards;
    @Column("createdat")
    private Timestamp createdAt;
    @Column("quotationid")
    private Integer quotationId;

    @Column("type_of_day_booking")
    private String typeOfDayBooking;

    @Column("additional_services")
    private String additionalServices;

    public static BookingEntity createBookingEntity(Integer userClientId, BookingSaveRequest bookingSaveRequest,
            Integer numberOfDays, Boolean isPromotor, Boolean isReceptionist) {
        ZoneId limaZoneId = ZoneId.of("America/Lima");
        /*
        Instant dayBookingInitInstant = bookingSaveRequest.getDayBookingInit()
                .atTime(15, 0)
                .atZone(limaZoneId) // Asigna la zona horaria de Lima
                .toInstant();      // Convierte a Instant (UTC)

        Instant dayBookingEndInstant = bookingSaveRequest.getDayBookingEnd()
                .atTime(12, 0)
                .atZone(limaZoneId) // Asigna la zona horaria de Lima
                .toInstant();      // Convierte a Instant (UTC)
         */
        return BookingEntity.builder()
                .roomOfferId(bookingSaveRequest.getRoomOfferId())
                .bookingStateId(3)
                .bookingId(
                        bookingSaveRequest.getBookingId())
                .userClientId((isPromotor || isReceptionist) ? bookingSaveRequest.getUserClientId() : userClientId)
                .userPromotorId(isPromotor ? userClientId : null)
                .receptionistId(isReceptionist ? userClientId : null)
                .numberAdults(bookingSaveRequest.getNumberAdult())
                .numberChildren(bookingSaveRequest.getNumberChild())
                .numberBabies(bookingSaveRequest.getNumberBaby())
                .numberAdultsMayor(bookingSaveRequest.getNumberAdultMayor())
                .numberAdultsExtra(bookingSaveRequest.getNumberAdultExtra())
                .infantCost(bookingSaveRequest.getInfantCost())
                .kidCost(bookingSaveRequest.getKidCost())
                .adultCost(bookingSaveRequest.getAdultCost())
                .adultMayorCost(bookingSaveRequest.getAdultMayorCost())
                .adultExtraCost(bookingSaveRequest.getAdultExtraCost())
                .dayBookingInit(Timestamp.valueOf(bookingSaveRequest.getDayBookingInit()
                        .atTime(15, 0)))
                .dayBookingEnd(Timestamp.valueOf(bookingSaveRequest.getDayBookingEnd()
                        .atTime(12, 0)))
                //.createdAt(Timestamp.valueOf(ZonedDateTime.now(limaZoneId).toLocalDateTime()))
                .createdAt(Timestamp.from(ZonedDateTime.now(limaZoneId).toInstant()))
                .quotationId(bookingSaveRequest.getQuotationId())
                .build();
    }

    public boolean hasPassed1Hours() {
        if (this.bookingStateId != null && this.bookingStateId == 2) {
            return false;
        }
        LocalDateTime createdTime = createdAt.toLocalDateTime();
        ZoneId limaZoneId = ZoneId.of("America/Lima");
        LocalDateTime currentTime = ZonedDateTime.now(limaZoneId).toLocalDateTime();
        Duration duration = Duration.between(createdTime, currentTime);

        return duration.toMinutes() >= 60;
    }
}