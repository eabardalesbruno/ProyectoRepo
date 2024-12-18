package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FeedingEntity;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedingRepository extends ReactiveCrudRepository<FeedingEntity, Integer> {
    @Query("SELECT * FROM feeding WHERE state = :state")
    Flux<FeedingEntity> findAllState(Integer state);
}
