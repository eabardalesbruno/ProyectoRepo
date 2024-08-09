package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.entity.TokenizeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PayMeRepository  extends R2dbcRepository<TokenizeEntity, Integer> {
    Flux<TokenizeEntity> findByIdUser(Integer idUser);
}
