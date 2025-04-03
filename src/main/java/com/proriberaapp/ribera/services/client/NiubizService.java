package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.NiubizAutorizationEntity;
import com.proriberaapp.ribera.Domain.entities.RewardPurchase;
import reactor.core.publisher.Mono;

public interface NiubizService {

    Mono<String> getSecurityToken();
    Mono<Object> getTokenSession(String token, Object body);
    Mono<String> tofinalize(NiubizAutorizationEntity niubizEntity, String token, Long purchaseNumber, Double amount, Integer type);
    Mono<Object> savePayNiubiz(Integer bookingId, String invoiceType, String invoiceDocumentNumber, Double totalDiscount, Double percentageDiscount, Double totalCostWithOutDiscount, Double amount, String transactionId);
    Mono<Object> savePayNiubizFullDay(Integer fullDayId, String invoiceType, String invoiceDocumentNumber, Double totalDiscount, Double percentageDiscount, Double totalCostWithOutDiscount, Double amount, String transactionId);
    Mono<String> purchaseRewards(String securityToken,
                                 String transactionToken,
                                 Long userId,
                                 int rewards);

    Mono<RewardPurchase> saveRewardPurchase(Long userId,
                                            int quantity,
                                            String transactionId,
                                            String purchaseNumber,
                                            double amount,
                                            String status);
}
