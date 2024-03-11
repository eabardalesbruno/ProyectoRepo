package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserLevelRepository;
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

    private final UserLevelRepository userLevelRepository;
    @Override
    public Mono<UserLevelEntity> save(UserLevelEntity userLevelEntity) {
        return userLevelRepository.findByLevelName(userLevelEntity.getLevelName())
                .switchIfEmpty(userLevelRepository.save(userLevelEntity));
    }

    @Override
    public Flux<UserLevelEntity> saveAll(Flux<UserLevelEntity> userLevelEntity) {
        return userLevelRepository.findByLevelName(userLevelEntity.map(UserLevelEntity::getLevelName))
                .collectList()
                .flatMapMany(userLevelEntities -> userLevelRepository.saveAll(
                        userLevelEntity.filter(
                                userLevelEntity1 -> !userLevelEntities.contains(userLevelEntity1))
                ));

    }

    @Override
    public Mono<UserLevelEntity> findById(String id) {
        return userLevelRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<UserLevelEntity> findAll() {
        return userLevelRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userLevelRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<UserLevelEntity> update(UserLevelEntity userLevelEntity) {
        return userLevelRepository.save(userLevelEntity);
    }
}
