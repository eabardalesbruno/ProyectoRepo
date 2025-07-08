package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreationResponse {
    private WalletData data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalletData {
        private Integer walletId;
        private Integer userId;
        private String message;
        private String cardNumber;
    }
} 