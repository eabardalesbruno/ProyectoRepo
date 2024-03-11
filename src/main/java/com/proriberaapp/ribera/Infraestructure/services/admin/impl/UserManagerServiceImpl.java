package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagerServiceImpl implements UserManagerService {
    @Override
    public Mono<UserEntity> updatePassword(Integer id, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserEntity> updatePassword(Integer id, String newPassword, String oldPassword) {
        return null;
    }

    @Override
    public Mono<UserEntity> requestUpdatePassword(String email) {
        return null;
    }

    @Override
    public Mono<UserEntity> findByEmail(String email) {
        return null;
    }

    @Override
    public Mono<UserEntity> findById(Integer id) {
        return null;
    }

    @Override
    public Flux<UserEntity> findAll() {
        return null;
    }

    @Override
    public Mono<UserEntity> disable(Integer id) {
        return null;
    }

    @Override
    public Mono<UserEntity> enable(Integer id) {
        return null;
    }

    @Override
    public Mono<UserEntity> delete(Integer id) {
        return null;
    }
}
