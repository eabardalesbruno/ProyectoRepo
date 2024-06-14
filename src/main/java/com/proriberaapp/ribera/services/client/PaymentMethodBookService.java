package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodBookService {
    Mono<PaymentMethodEntity> createPaymentMethod(PaymentMethodEntity paymentMethod);
    Mono<PaymentMethodEntity> updatePaymentMethod(Integer id, PaymentMethodEntity paymentMethod);
    Mono<PaymentMethodEntity> getPaymentMethodById(Integer id);
    Flux<PaymentMethodEntity> getAllPaymentMethods();
    Mono<Void> deletePaymentMethod(Integer id);
}
