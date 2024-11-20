package com.proriberaapp.ribera.Domain.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.security.Timestamp;

@Getter
@Setter
@Builder
@Table("wallettransaction")
public class WalletTransactionEntity {


    @Id
    @Column("wallettransactionid")
    private Integer walletTransactionId;
    @Column("walletid")
    private Integer walletId;

    @Column("currencytypeid")
    private Integer currencyTypeId;
/*
    @Column("typewallettransactionid")
    private Integer typeWalletTransactionId;
 */

    @Column("transactioncategoryid")
    private Integer transactionCategoryId;

    @Column("inicialdate")
    private Timestamp inicialDate;

    @Column("amount")
    private BigDecimal amount;

    @Column("avalibledate")
    private Timestamp avalibleDate;

    @Column("description")
    private String description;

}
