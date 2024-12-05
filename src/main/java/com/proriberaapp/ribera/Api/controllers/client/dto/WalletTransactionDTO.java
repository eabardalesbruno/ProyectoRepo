package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class WalletTransactionDTO {
    private String username;
    private Timestamp inicialDate;
    private BigDecimal amount;
    private String description;
    private String operationCode;
}
