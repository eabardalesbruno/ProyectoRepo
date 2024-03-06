package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Integer> {

    Flux<UserEntity> findAll();
    Mono<UserEntity> findById(Integer id);
    Mono<UserEntity> save(UserEntity user);
    Mono<Void> deleteById(Integer id);

    @Query("SELECT * FROM public.user WHERE email = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("SELECT * FROM public.user WHERE google_id = :googleId")
    Mono<UserEntity> findByGoogleId(String googleId);
}
