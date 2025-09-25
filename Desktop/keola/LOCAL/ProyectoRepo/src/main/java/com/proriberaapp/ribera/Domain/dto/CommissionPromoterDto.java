package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class CommissionPromoterDto {
    @Column("commissionid")
    private Integer commissionid;
    @Column("serialnumber")
    private String serialnumber;
    @Column("createdat")
    private Timestamp createdat;
    @Column("currencytypeid")
    private Integer currencytypeid;
    @Column("commissionamount")
    private BigDecimal commissionamount;
    @Column("rucnumber")
    private String rucnumber;
    @Column("status")
    private String status;
    @Column("dateofapplication")
    private Timestamp dateofapplication;
    @Column("invoicedocument")
    private String invoicedocument;
    @Column("bookingid")
    private Integer bookingid;
}
