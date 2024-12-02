package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientRepository extends R2dbcRepository<UserClientEntity, Integer> {
    Mono<UserClientEntity> findByEmail(String email);

    Mono<UserClientEntity> findByGoogleId(String googleId);

    @Query(value = "SELECT * FROM userclient u WHERE u.documentnumber = :documentNumber")

    Mono<UserClientEntity> findByDocumentNumber(@Param("documentNumber") String documentNumber);

    UserDataDTO save(UserDataDTO userDataDTO);

    Flux<UserClientEntity> findAll();

    Mono<Void> deleteById(Integer id);

    Mono<UserClientEntity> findByUserClientId(Integer userClientId);

    Mono<UserClientEntity> findByUsername(String username);

    @Query("SELECT * FROM userclient WHERE email = :email OR googleid = :googleId OR googleemail = :googleEmail")
    Mono<UserClientEntity> findByEmailOrGoogleIdOrGoogleEmail(String email, String googleId, String googleEmail);

    @Query("SELECT * FROM userclient uc WHERE uc.userclientid IN (SELECT b.userclientid FROM booking b WHERE b.userpromotorid = :userpromotorid)")
    Flux<UserClientEntity> findByUserPromotorId(Integer userpromotorid);

}
