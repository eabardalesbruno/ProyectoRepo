package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import reactor.core.publisher.Mono;

public interface TokenPointsTransactionService {
    Mono<TokenPointsTransaction> createToken(Integer partnerPointId, Integer bookingId);
    Mono<TokenPointsTransaction> createTokenAndSendEmail(Integer partnerPointId, Integer bookingId);

}
