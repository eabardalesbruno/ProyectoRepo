package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserLevelRepository extends R2dbcRepository<UserLevelEntity, Integer> {
    Mono<UserLevelEntity> findByLevelName(String levelName);
    Flux<UserLevelEntity> findAllByLevelNameIn(List<UserLevelEntity> levelName);
}
