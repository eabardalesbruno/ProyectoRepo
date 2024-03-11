package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.management.monitor.Monitor;

public interface UserLevelService {
    Mono<UserLevelEntity> save(UserLevelEntity userLevelEntity);
    Flux<UserLevelEntity> saveAll(Flux<UserLevelEntity> userLevelEntity);
    Mono<UserLevelEntity> findById(String id);
    Flux<UserLevelEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<UserLevelEntity> update(UserLevelEntity userLevelEntity);

}
