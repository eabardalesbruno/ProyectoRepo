package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Table("bookingdetail")
public class BookingDetailEntity {
    @Id
    @Column("bookingdetailid")
    private Integer bookingDetailId;
    @Column("bookingid")
    private Integer bookingId;
    @Column("userclientid")
    private Integer userClientId;
    @Column("checkin")
    private LocalDateTime checkIn;
    @Column("checkout")
    private LocalDateTime checkOut;
    private BigDecimal costFinal;
}