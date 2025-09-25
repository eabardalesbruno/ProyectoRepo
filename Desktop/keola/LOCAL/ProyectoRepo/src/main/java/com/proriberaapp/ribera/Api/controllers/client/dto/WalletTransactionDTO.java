package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class WalletTransactionDTO {
    private Integer walletTransactionId;
    private Integer walletId;
    private Integer currencyTypeId;
    private Integer transactionCategoryId;
    private Timestamp inicialDate;
    private BigDecimal amount;
    private Timestamp avalibleDate;
    private String description;
    private String motivedescription;
    private String operationCode;
    private String transactioncategoryname;
}
