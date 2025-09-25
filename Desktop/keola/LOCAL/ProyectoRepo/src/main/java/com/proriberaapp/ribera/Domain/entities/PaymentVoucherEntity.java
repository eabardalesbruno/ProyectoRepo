package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Table("paymentvoucher")
public class PaymentVoucherEntity {

    @Id
    @Column("voucherid")
    private Integer voucherId;

    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("paymenttypeid")
    private Integer paymentTypeId;

    @Column("paymentsubtypeid")
    private Integer paymentSubTypeId;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("operationcode")
    private String operationCode;

    @Column("note")
    private String note;

    @Column("amount")
    private BigDecimal amount;

    @Column("imagevoucher")
    private String imageVoucher;

    @Column("usdrewardsinclub")
    private Boolean usdRewardsInclub;
}