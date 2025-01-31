package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.FamilyGroupEntity;

import reactor.core.publisher.Flux;

@Repository
public interface FamilyGroupRepository extends ReactiveCrudRepository<FamilyGroupEntity, Integer> {
    Flux<FamilyGroupEntity> findAllByStatus(Integer status);
}
