package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("commission")
public class CommissionEntity {
    @Id
    @Column("commissionid")
    private Integer commissionId;

    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("promoterid")
    private Integer promoterId;

    @Column("partnerid")
    private Integer partnerId;

    @Column("commissionamount")
    private BigDecimal commissionAmount;

    @Column("riberaamount")
    private BigDecimal riberaAmount;

    @Column("partnerpayment")
    private BigDecimal partnerPayment;

    @Column("adminfee")
    private BigDecimal adminFee;

    @Column("servicefee")
    private BigDecimal serviceFee;

    @Column("casetype")
    private Integer caseType;

    @Column("createdat")
    private Timestamp createdAt;

    @Column("disbursementdate")
    private Timestamp disbursementDate;

    @Column("invoicedocument")
    private String invoiceDocument;

    @Column("processed")
    private boolean processed;

    @Column("processedat")
    private Timestamp processedAt;
}
