package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientRepository extends R2dbcRepository<UserClientEntity, Integer> {
    Mono<UserClientEntity> findByEmail(String email);
    Mono<UserClientEntity> findByGoogleId(String googleId);
    Mono<UserClientEntity> findByDocumentNumber(String documentNumber);
    UserDataDTO save(UserDataDTO userDataDTO);
    Flux<UserClientEntity> findAll();
    Mono<UserClientEntity> findById(Integer id);
    Mono<Void> deleteById(Integer id);
    Mono<UserClientEntity> findByUserClientId(Integer userClientId);

    @Query("SELECT * FROM userclient WHERE email = :email OR googleid = :googleId OR googleemail = :googleEmail")
    Mono<UserClientEntity> findByEmailOrGoogleIdOrGoogleEmail(String email, String googleId, String googleEmail);

}
