package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.ClientManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientManagerServiceImpl implements ClientManagerService {
    private final UserRepository userRepository;
    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userRepository::save);
    }

    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword, String oldPassword) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userRepository::save);
    }

    @Override
    public Mono<UserClientEntity> requestUpdatePassword(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserClientEntity> disable(Integer id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.INACTIVE);
            return user;
        }).flatMap(userRepository::save);
    }

    @Override
    public Mono<UserClientEntity> enable(Integer id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.ACTIVE);
            return user;
        }).flatMap(userRepository::save);
    }

    @Override
    public Mono<UserClientEntity> delete(Integer id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.DELETED);
            return user;
        }).flatMap(userRepository::save);
    }
}
