package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodService {
    Mono<PaymentMethodEntity> save(PaymentMethodRequest paymentMethodEntity);
    Flux<PaymentMethodEntity> saveAll(Flux<PaymentMethodRequest> paymentMethodEntity);
    Mono<PaymentMethodEntity> findById(String id);
    Flux<PaymentMethodEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<PaymentMethodEntity> update(PaymentMethodEntity paymentMethodEntity);
}
