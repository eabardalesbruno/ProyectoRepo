package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserEntity> registerUser(UserEntity user);
    Mono<String> login(String email, String password);
    Mono<UserEntity> registerWithGoogle(String googleId, String email, String name);
    Mono<String> loginWithGoogle(String googleId);
    Mono<UserEntity> saveUser(UserEntity user);
    UserDataDTO searchUser(String username);
    UserDataDTO registerUser(UserDataDTO userDataDTO);
    String loginUser(String username, String password);
    UserEntity findByEmail(String email);

    void updatePassword(UserEntity user, String newPassword);
}
