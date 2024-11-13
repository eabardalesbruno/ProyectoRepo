package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserPromoterRepository extends R2dbcRepository<UserPromoterEntity, Integer> {
    Mono<UserPromoterEntity> findByUsernameOrEmail(String username, String email);

    Mono<UserPromoterEntity> findByUsernameOrEmailOrDocumentNumber(String username, String email, String document);

    Mono<UserPromoterEntity> findById(Integer id);
}


