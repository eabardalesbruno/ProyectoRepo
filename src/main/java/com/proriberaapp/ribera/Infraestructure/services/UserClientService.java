package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
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

    void updatePassword(UserClientEntity user, String newPassword);
}
