package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository {

    Flux<UserEntity> findAll();
    Mono<UserEntity> findById(Integer id);
    Mono<UserEntity> save(UserEntity user);
    Mono<Void> deleteById(Integer id);
}
