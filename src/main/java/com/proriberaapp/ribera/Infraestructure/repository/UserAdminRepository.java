package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserAdminRepository extends R2dbcRepository<UserAdminEntity, Integer> {
    Mono<UserAdminEntity> findByUsernameOrEmail(String username, String email);
    Mono<UserAdminEntity> findByUsernameOrEmailOrDocumentNumber(String username, String email, String document);
    Mono<UserAdminEntity> findByEmail(String email);
    Mono<UserAdminEntity> findByEmailAndDocumentNumber(String email, String document);
}
