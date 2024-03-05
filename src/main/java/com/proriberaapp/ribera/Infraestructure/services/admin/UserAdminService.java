package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UpdateUserAdminRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserAdminResponse;
import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import org.springframework.data.relational.core.sql.In;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAdminService {
    Mono<UserAdminResponse> login(LoginRequest loginRequest);
    Mono<UserAdminResponse> register(RegisterRequest userAdminEntity);
    Mono<UserAdminResponse> update(Integer id, UpdateUserAdminRequest updateUserAdminRequest);
    Mono<UserAdminResponse> updatePassword(Integer id, String newPassword);
    Mono<UserAdminResponse> delete(Integer id);
    Mono<UserAdminResponse> findByEmail(String email);

    //Mono<UserAdminResponse> disable(Integer id); //TODO: Implementar
    //Mono<UserAdminResponse> enable(Integer id);   //TODO: Implementar
    Mono<UserAdminResponse> findById(Integer id);
    Flux<UserAdminResponse> findAll();
}
