package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PaymentSubtypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentSubtypeService {
    Mono<PaymentSubtypeEntity> createPaymentSubtype(PaymentSubtypeEntity paymentSubtype);
    Mono<PaymentSubtypeEntity> getPaymentSubtype(Integer paymentSubtypeId);
    Flux<PaymentSubtypeEntity> getAllPaymentSubtypes();
    Mono<Void> deletePaymentSubtype(Integer paymentSubtypeId);
}
