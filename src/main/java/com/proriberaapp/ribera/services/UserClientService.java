package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientService {
    Mono<UserClientEntity> registerUser(UserClientEntity user);
    Mono<String> login(String email, String password);
    Mono<UserClientEntity> registerWithGoogle(String googleId, String email, String name);
    Mono<String> loginWithGoogle(String googleId);
    Mono<UserClientEntity> saveUser(UserClientEntity user);
    UserDataDTO searchUser(String username);
    UserDataDTO registerUser(UserDataDTO userDataDTO);
    String loginUser(String username, String password);
    Mono<UserClientEntity> findByEmail(String email);

    Flux<UserClientEntity> findAll();
    Mono<UserClientEntity> findById(Integer id);
    Mono<Void> deleteById(Integer id);
    boolean existsByEmail(String email);

    Mono<UserClientEntity> updatePassword(UserClientEntity user, String newPassword);



}
