package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import com.proriberaapp.ribera.Infraestructure.services.UserLevelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLevelServiceImpl implements UserLevelService {
    @Override
    public Mono<UserLevelEntity> save(UserLevelEntity userLevelEntity) {
        return null;
    }

    @Override
    public Flux<UserLevelEntity> saveAll(Flux<UserLevelEntity> userLevelEntity) {
        return null;
    }

    @Override
    public Mono<UserLevelEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<UserLevelEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<UserLevelEntity> update(UserLevelEntity userLevelEntity) {
        return null;
    }
}
