package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FinalCostumerRepository extends R2dbcRepository<FinalCostumerEntity, Integer>{
    Mono<FinalCostumerRepository> findByDocumentNumber(FinalCostumerEntity entity);
    Flux<FinalCostumerRepository> findAllByDocumentNumberIn(List<FinalCostumerEntity> entity);
    //Terrible
}
