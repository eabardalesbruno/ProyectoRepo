package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PaymentStateRepository extends R2dbcRepository<PaymentStateEntity, Integer> {

    Mono<Object> findByPaymentStateName(String paymentStateName);
    Flux<Object> findAllByPaymentStateNameIn(List<PaymentStateEntity> paymentStateName);
}
