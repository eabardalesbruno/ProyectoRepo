package com.proriberaapp.ribera.Domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletDto {
    private Integer walletId;
    private BigDecimal balance;
}
