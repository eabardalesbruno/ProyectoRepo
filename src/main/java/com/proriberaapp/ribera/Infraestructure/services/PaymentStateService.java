package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentStateService {
    Mono<PaymentStateEntity> save(PaymentStateEntity paymentStateEntity);
    Flux<PaymentStateEntity> saveAll(Flux<PaymentStateEntity> paymentStateEntity);
    Mono<PaymentStateEntity> findById(String id);
    Flux<PaymentStateEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<PaymentStateEntity> update(PaymentStateEntity paymentStateEntity);
}
