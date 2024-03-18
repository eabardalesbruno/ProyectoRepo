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

    @Column("bookingdetailid")
    private Integer bookingDetailId;

    @Column("paymentmethodid")
    private Integer paymentMethodId;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("paymentstateid")
    private Integer paymentStateId;

    @Column("refusereasonid")
    private Integer refuseReasonId;

    private BigDecimal amount;

    private String description;

    @Column("paymentdate")
    private Timestamp paymentDate;

    @Column("operationcode")
    private String operationCode;

    private String note;

    @Column("totalcost")
    private BigDecimal totalCost;

    @Column("imagevoucher")
    private String imageVoucher;

    @Column("totalpoints")
    private Integer totalPoints;

    @Column("paymentcomplete")
    private Boolean paymentComplete;
}