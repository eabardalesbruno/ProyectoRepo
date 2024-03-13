package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserClientEntity, Integer> {
    Mono<UserClientEntity> findByEmail(String email);
    Mono<UserClientEntity> findByGoogleId(String googleId);
    Mono<UserClientEntity> findByDocumentNumber(String documentNumber);
    UserDataDTO save(UserDataDTO userDataDTO);


}
