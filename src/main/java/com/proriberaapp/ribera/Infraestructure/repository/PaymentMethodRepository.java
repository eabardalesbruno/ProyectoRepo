package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity.StatePaymentMethod;

public interface PaymentMethodRepository extends R2dbcRepository<PaymentMethodEntity, Integer> {

    Mono<Object> findByDescription(String description);

    Flux<Object> findAllByDescriptionIn(List<PaymentMethodRequest> paymentMethodEntity);

    Flux<PaymentMethodEntity> findAllByState(StatePaymentMethod state);
}
