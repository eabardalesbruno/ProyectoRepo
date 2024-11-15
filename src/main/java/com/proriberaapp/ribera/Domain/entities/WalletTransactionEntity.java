package com.proriberaapp.ribera.Domain.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    @Column("inicialdate")
    private Timestamp inicialDate;

    @Column("amount")
    private Double amount;

    @Column("avalibledate")
    private Timestamp avalibleDate;

    @Column("description")
    private String description;

    @Column("suscessfultransaction")
    private String sucessfulTransaction;














}
