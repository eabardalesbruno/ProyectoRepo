package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.FeedingTypeEntity;

import reactor.core.publisher.Flux;

@Repository
public interface FeedingTypeRepository extends ReactiveCrudRepository<FeedingTypeEntity, Integer> {

    Flux<FeedingTypeEntity> findAllByStatus(Integer status);
}
