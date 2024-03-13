package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
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
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword, String oldPassword) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> requestUpdatePassword(String email) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return null;
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return null;
    }

    @Override
    public Mono<UserClientEntity> disable(Integer id) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> enable(Integer id) {
        return null;
    }

    @Override
    public Mono<UserClientEntity> delete(Integer id) {
        return null;
    }
}
