package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CalendarDate {
    @Column("bookingid")
    Integer bookingId;
    @Column("roomofferid")
    Integer roomOfferId;
    @Column("daybookinginit")
    Timestamp dayBookingInit;
    @Column("daybookingend")
    Timestamp dayBookingEnd;
}
