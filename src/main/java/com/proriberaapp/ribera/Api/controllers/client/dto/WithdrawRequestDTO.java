package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WithdrawRequestDTO {
    private Integer walletId;
    private BigDecimal amount;
    private String country;
    private String bank;
    private String accountNumber;
    private String documentType;
    private String documentNumber;
    private String holderFirstName;
    private String holderLastName;
}
