package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAdminManagerService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserAdminResponse> register(Integer idUserAdmin, RegisterRequest userAdminEntity);
    Mono<UserAdminResponse> update(Integer id, Integer idUserAdminUpdate, UpdateUserAdminRequest updateUserAdminRequest);
    Mono<UserAdminResponse> updatePassword(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword);
    Mono<UserAdminResponse> updatePasswordBySolicitude(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword, String oldPassword);
    Mono<UserAdminResponse> updatePasswordByVerificationCode(String verificationCode, String newPassword);
    Mono<UserAdminResponse> updatePasswordByVerificationCode(Integer idUserAdmin, String verificationCode, String newPassword);

    Mono<UserAdminResponse> requestUpdatePassword(RequestUpdateUserAdminRequest requestUpdateRequest);
    Mono<UserAdminResponse> findByEmail(String email);
    Mono<UserAdminResponse> findById(Integer id);
    Flux<UserAdminResponse> findAll();
    Mono<UserAdminResponse> disable(Integer idUserAdmin, Integer idUserAdminUpdateStatus);
    Mono<UserAdminResponse> enable(Integer idUserAdmin, Integer idUserAdminUpdateStatus);
    Mono<UserAdminResponse> delete(Integer idUserAdmin, Integer idUserAdminDelete);

}
