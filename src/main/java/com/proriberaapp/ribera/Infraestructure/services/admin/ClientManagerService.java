package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientManagerService {
    Mono<UserClientEntity> updatePassword(Integer id, String newPassword);
    Mono<UserClientEntity> updatePassword(Integer id, String newPassword, String oldPassword);
    Mono<UserClientEntity> requestUpdatePassword(String email);
    Mono<UserClientEntity> findByEmail(String email);
    Mono<UserClientEntity> findById(Integer id);
    Flux<UserClientEntity> findAll();
    Mono<UserClientEntity> disable(Integer id);
    Mono<UserClientEntity> enable(Integer id);
    Mono<UserClientEntity> delete(Integer id);
}
