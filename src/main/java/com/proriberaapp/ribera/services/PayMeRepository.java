package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.dto.PaymentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PayMeRepository  extends R2dbcRepository<PaymentEntity, Integer> {
    Flux<PaymentEntity> findByIdUser(Integer idUser);
}
