package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.PaymentTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentTypeService {
    Mono<PaymentTypeEntity> createPaymentType(PaymentTypeEntity paymentType);
    Mono<PaymentTypeEntity> getPaymentType(Integer paymentTypeId);
    Flux<PaymentTypeEntity> getAllPaymentTypes();
    Mono<Void> deletePaymentType(Integer paymentTypeId);
}
