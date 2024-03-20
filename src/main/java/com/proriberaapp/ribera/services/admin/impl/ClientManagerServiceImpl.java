package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.admin.ClientManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientManagerServiceImpl implements ClientManagerService {
    private final UserClientRepository userClientRepository;
    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword) {
        return userClientRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword, String oldPassword) {
        return userClientRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> requestUpdatePassword(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return userClientRepository.findById(id);
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return userClientRepository.findAll();
    }

    @Override
    public Mono<UserClientEntity> disable(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.INACTIVE);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> enable(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.ACTIVE);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> delete(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.DELETED);
            return user;
        }).flatMap(userClientRepository::save);
    }
}
