package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAdminService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserAdminResponse> register(Integer idUserAdmin, RegisterRequest userAdminEntity);
    Mono<UserAdminResponse> update(Integer id, Integer idUserAdminUpdate, UpdateUserAdminRequest updateUserAdminRequest);
    Mono<UserAdminResponse> updatePassword(Integer id, Integer idUserAdminUpdatePassword, String newPassword);
    Mono<Void> delete(Integer id, Integer idUserAdminDelete);
    Mono<UserAdminResponse> findByEmail(String email);

    Mono<UserAdminResponse> disable(Integer id); //TODO: Implementar
    Mono<UserAdminResponse> enable(Integer id);   //TODO: Implementar
    Mono<UserAdminResponse> findById(Integer id);
    Flux<UserAdminResponse> findAll(Integer idUserAdmin);
}
