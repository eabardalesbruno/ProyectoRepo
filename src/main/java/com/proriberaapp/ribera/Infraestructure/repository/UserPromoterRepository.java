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
    Mono<UserPromoterEntity> findByEmail(String email);

    @Query(value = "SELECT * FROM userpromoter u WHERE u.documentnumber = :documentNumber")
    Mono<UserPromoterEntity> findByDocumentNumber(@Param("documentNumber") String documentNumber);
    Mono<UserPromoterEntity> findById(Integer id);
}


