package com.proriberaapp.ribera.services.client;



import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;
import com.proriberaapp.ribera.Api.controllers.client.dto.WithdrawRequestDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletTransactionService {

    Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny , String emailDestiny, String cardNumber, BigDecimal amount , String motiveDescription);
    Mono<WalletTransactionEntity> makeWithdrawal(WithdrawRequestDTO withdrawRequest);
    Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<String> makePayment(Integer walletId, List<Integer> bookingIds);
    Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletEntity> findWalletByEmail(String email);
    Mono<BigDecimal> getTotalAmountForPromoter(Integer userPromoterId);
    Mono<Map<String, Object>> getBookingDetailsForPromoter(Integer walletId);
    Map<String, Object> getExchangeRate(String date);
}
