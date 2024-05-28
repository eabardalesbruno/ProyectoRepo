package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Table("paymentbook")
public class PaymentBookEntity {
    @Id
    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("bookingid")
    private Integer bookingId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("refusereasonid")
    private Integer refuseReasonId;

    @Column("paymentmethodid")
    private Integer paymentMethodId;

    @Column("paymentstateid")
    private Integer paymentStateId;

    @Column("paymenttypeid")
    private Integer paymentTypeId;

    @Column("paymentsubtypeid")
    private Integer paymentSubTypeId;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("amount")
    private BigDecimal amount;

    @Column("description")
    private String description;

    @Column("paymentdate")
    private Timestamp paymentDate;

    @Column("operationcode")
    private String operationCode;

    @Column("note")
    private String note;

    @Column("totalcost")
    private BigDecimal totalCost;

    @Column("imagevoucher")
    private String imageVoucher;

    @Column("totalpoints")
    private Integer totalPoints;

    @Column("paymentcomplete")
    private Boolean paymentComplete;

    @Column("pendingpay")
    private Integer pendingpay;

    @Column("paymenttoken")
    private String paymenttoken;

    @Column("tokenexpiration")
    private Timestamp tokenexpiration;

    @Column("email")
    private String email;
}