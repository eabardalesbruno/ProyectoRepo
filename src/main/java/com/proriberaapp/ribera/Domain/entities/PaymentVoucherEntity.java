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

    @Column("currency")
    private Integer currency;

    @Column("operation")
    private Integer operation;

    @Column("codeoperation")
    private String codeOperation;

    @Column("note")
    private String note;

    @Column("totalcost")
    private BigDecimal totalCost;

    @Column("filename")
    private String fileName;
}