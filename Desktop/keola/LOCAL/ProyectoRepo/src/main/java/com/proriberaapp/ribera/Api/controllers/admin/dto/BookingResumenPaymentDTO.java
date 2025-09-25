package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingResumenPaymentDTO {

    @Column("invoice_createdat")
    private String createDatInvoice;

    @Column("roomtypename")
    private String roomTypeName;

    @Column("totalcost")
    private BigDecimal totalCost;

}
