package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodService {
    Mono<PaymentMethodEntity> save(PaymentMethodEntity paymentMethodEntity);
    Flux<PaymentMethodEntity> saveAll(Flux<PaymentMethodEntity> paymentMethodEntity);
    Mono<PaymentMethodEntity> findById(String id);
    Flux<PaymentMethodEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<PaymentMethodEntity> update(PaymentMethodEntity paymentMethodEntity);
}
