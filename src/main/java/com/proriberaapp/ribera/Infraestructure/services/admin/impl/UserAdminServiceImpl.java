package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UpdateUserAdminRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserAdminResponse;
import com.proriberaapp.ribera.Infraestructure.repository.UserAdminRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAdminRepository userAdminRepository;

    @Override
    public Mono<UserAdminResponse> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> register(RegisterRequest userAdminEntity) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> update(Integer id, UpdateUserAdminRequest updateUserAdminRequest) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> updatePassword(Integer id, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> delete(Integer id) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> findByEmail(String email) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> findById(Integer id) {
        return null;
    }

    @Override
    public Flux<UserAdminResponse> findAll() {
        return null;
    }
}
