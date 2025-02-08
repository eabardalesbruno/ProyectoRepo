package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class CommissionAdminDto {

    @Column("paymentbookid")
    private Integer paymentbookid;

    @Column("bookingid")
    private Integer bookingid;

    @Column("bookingstateid")
    private Integer bookingstateid;

    @Column("firstname")
    private String  firstname;

    @Column("lastname")
    private String  lastname;

    @Column("costfinal")
    private BigDecimal costfinal;

    @Column("bookingfeedingamout")
    private BigDecimal bookingfeedingamout;

    @Column("montosinalimentos")
    private BigDecimal montosinalimentos;

    @Column("roomname")
    private String roomname;

    @Column("dateofapplication")
    private Timestamp dateofapplication;

    @Column("currencytypeid")
    private Integer currencytypeid;


}
