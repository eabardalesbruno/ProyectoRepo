package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentSubtypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentSubtypeRepository extends R2dbcRepository<PaymentSubtypeEntity, Integer> {
    Flux<PaymentSubtypeEntity> findByPaymentTypeId(Integer paymentTypeId);
}