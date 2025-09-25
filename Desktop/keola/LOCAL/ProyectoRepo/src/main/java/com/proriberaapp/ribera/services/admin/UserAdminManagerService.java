package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAdminManagerService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserResponse> register(Integer idUserAdmin, RegisterRequest userAdminEntity);
    Mono<UserResponse> update(Integer id, Integer idUserAdminUpdate, UpdateUserAdminRequest updateUserAdminRequest);
    Mono<UserResponse> updatePassword(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword);
    Mono<UserResponse> updatePasswordBySolicitude(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword, String oldPassword);
    Mono<UserResponse> updatePasswordByVerificationCode(String verificationCode, String newPassword);
    Mono<UserResponse> updatePasswordByVerificationCode(Integer idUserAdmin, String verificationCode, String newPassword);
    Mono<UserResponse> requestUpdatePassword(RequestUpdateUserAdminRequest requestUpdateRequest);
    Mono<UserResponse> findByEmail(String email);
    Mono<UserResponse> findById(Integer id);
    Flux<UserResponse> findAll();
    Mono<UserResponse> disable(Integer idUserAdmin, Integer idUserAdminUpdateStatus);
    Mono<UserResponse> enable(Integer idUserAdmin, Integer idUserAdminUpdateStatus);
    Mono<UserResponse> delete(Integer idUserAdmin, Integer idUserAdminDelete);

}
