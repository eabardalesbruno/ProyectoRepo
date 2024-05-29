package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PaymentTokenEntity;
import reactor.core.publisher.Mono;

public interface PaymentTokenService {

    Mono<String> generateAndSaveToken(Integer bookingId, Integer paymentBookId);

}