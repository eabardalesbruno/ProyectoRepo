package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStateServiceImpl implements PaymentStateService {
    @Override
    public Mono<PaymentStateEntity> save(PaymentStateEntity paymentStateEntity) {
        return null;
    }

    @Override
    public Flux<PaymentStateEntity> saveAll(Flux<PaymentStateEntity> paymentStateEntity) {
        return null;
    }

    @Override
    public Mono<PaymentStateEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<PaymentStateEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<PaymentStateEntity> update(PaymentStateEntity paymentStateEntity) {
        return null;
    }
}
