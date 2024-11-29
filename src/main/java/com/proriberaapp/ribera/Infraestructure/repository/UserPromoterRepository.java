package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface UserPromoterRepository extends R2dbcRepository<UserPromoterEntity, Integer> {
    Mono<UserPromoterEntity> findByUsernameOrEmail(String username, String email);
    Mono<UserPromoterEntity> findByUsernameOrEmailOrDocumentNumber(String username, String email, String document);
    Mono<UserPromoterEntity> findById(Integer id);

    Mono<UserPromoterEntity> findByGoogleId(String googleId);

    @Query("SELECT * FROM userpromoter WHERE email = :email OR googleid = :googleId OR googleemail = :googleEmail")
    Mono<UserPromoterEntity> findByEmailOrGoogleIdOrGoogleEmail(String email, String googleId, String googleEmail);

    Mono<UserPromoterEntity> findByEmail(String email);
    Mono<UserPromoterEntity> findByDocumentNumber(String documentNumber);
}


