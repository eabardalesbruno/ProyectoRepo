package com.proriberaapp.ribera.Domain.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class PaymentDetailFulldayDTO {

    @Column("paymentdate")
    private Timestamp paymentDate;

    @Column("imagevoucher")
    private String imageVoucher;

    @Column("paymentmethodid")
    private Integer paymentMethodId;

    @Column("paymentmethod_description")
    private String paymentMethodDescription;

    @Column("totalprice")
    private BigDecimal totalPrice;

    @Column("client_firstname")
    private String clientFirstName;

    @Column("client_lastname")
    private String clientLastName;

    @Column("documentnumber")
    private String documentNumber;

    @Column("documenttypeid")
    private String documenttypeid;

    @Column("channel")
    private String channel;

    @Column("paymentstateid")
    private Integer paymentstateid;

    @Column("paymentbookid")
    private Integer paymentbookid;
}
