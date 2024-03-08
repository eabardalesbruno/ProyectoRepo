package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserManagerService {
    Mono<UserEntity> updatePassword(Integer id, String newPassword);
    Mono<UserEntity> updatePassword(Integer id, String newPassword, String oldPassword);
    Mono<UserEntity> requestUpdatePassword(String email);
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findById(Integer id);
    Flux<UserEntity> findAll();
    Mono<UserEntity> disable(Integer id);
    Mono<UserEntity> enable(Integer id);
    Mono<UserEntity> delete(Integer id);
}
