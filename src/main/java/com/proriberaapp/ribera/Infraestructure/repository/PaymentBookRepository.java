package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PaymentBookRepository extends R2dbcRepository<PaymentBookEntity, Integer> {
    Flux<PaymentBookEntity> findAll();
}