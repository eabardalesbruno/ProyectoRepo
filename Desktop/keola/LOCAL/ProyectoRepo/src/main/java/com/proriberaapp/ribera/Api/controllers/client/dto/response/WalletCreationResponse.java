package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreationResponse {
    private boolean success;
    private String message;
    private Integer walletId;
    private String walletType;
    private List<WalletDetailResponse> walletDetails;
    private BigDecimal totalBalance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalletDetailResponse {
        private Integer id;
        private String portfolioCode;
        private String portfolioName;
        private BigDecimal balance;
        private String status;
    }
} 