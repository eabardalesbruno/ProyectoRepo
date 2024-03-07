package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserEntity> registerUser(UserEntity user);
    Mono<String> login(String email, String password);
    Mono<UserEntity> registerWithGoogle(String googleId, String email, String name);
    Mono<String> loginWithGoogle(String googleId);
    Mono<UserEntity> saveUser(UserEntity user); // Nuevo método para guardar un usuario sin codificar la contraseña

}
