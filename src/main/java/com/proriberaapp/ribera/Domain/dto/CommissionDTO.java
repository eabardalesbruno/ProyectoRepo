package com.proriberaapp.ribera.Domain.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class CommissionDTO {
    @Column("commissionid")
    private Integer commissionId;

    @Column("disbursementdate")
    private Timestamp disbursementDate;

    @Column("promoterid")
    private Integer promoterId;

    @Column("promoter_fullname")
    private String promoterFullName;

    @Column("rucnumber")
    private String rucNumber;

    @Column("commissionamount")
    private BigDecimal commissionAmount;

    @Column("status")
    private String status;

    @Column("invoicedocument")
    private String invoiceDocument;

    @Column("processed")
    private boolean processed;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("total_commissions")
    private Integer total_commissions;
}
