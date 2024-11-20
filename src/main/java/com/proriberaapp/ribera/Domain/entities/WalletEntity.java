package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Table("wallet")
public class WalletEntity {

    @Id
    @Column("walletid")
    private Integer walletId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("userpromoterid")
    private Integer userPromoterId;

    @Column("cardnumber")
    private String cardNumber;

    @Column("balance")
    private BigDecimal balance;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("avalible")
    private boolean avalible;





}
