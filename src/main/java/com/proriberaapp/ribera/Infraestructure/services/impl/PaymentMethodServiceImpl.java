package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Override
    public Mono<PaymentMethodEntity> save(PaymentMethodEntity paymentMethodEntity) {
        return null;
    }

    @Override
    public Flux<PaymentMethodEntity> saveAll(Flux<PaymentMethodEntity> paymentMethodEntity) {
        return null;
    }

    @Override
    public Mono<PaymentMethodEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<PaymentMethodEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<PaymentMethodEntity> update(PaymentMethodEntity paymentMethodEntity) {
        return null;
    }
}
