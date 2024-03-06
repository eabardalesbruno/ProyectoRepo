package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.admin.exception.CustomException;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.enums.States;
import com.proriberaapp.ribera.Infraestructure.repository.UserAdminRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userAdminRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == States.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")))
                .map(user -> new TokenDto(jwtTokenProvider.generateTokenAdmin(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "error generating token")));
    }

    @Override
    public Mono<UserAdminResponse> register(RegisterRequest userAdminEntity) {

        String password = passwordEncoder.encode(userAdminEntity.password());
        UserAdminEntity user = RegisterRequest.from(userAdminEntity, password);
        Mono<Boolean> userExists = userAdminRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).hasElement();
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new RuntimeException("username or email already in use"))
                        : userAdminRepository.save(user))
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> update(Integer id, UpdateUserAdminRequest updateUserAdminRequest) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setUsername(updateUserAdminRequest.username());
                    user.setFirstName(updateUserAdminRequest.firstName());
                    user.setLastName(updateUserAdminRequest.lastName());
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> updatePassword(Integer id, String newPassword) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return userAdminRepository.findById(id)
                .flatMap(userAdminRepository::delete);
    }

    @Override
    public Mono<UserAdminResponse> findByEmail(String email) {
        return userAdminRepository.findByEmail(email)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> findById(Integer id) {
        return userAdminRepository.findById(id)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Flux<UserAdminResponse> findAll() {
        return userAdminRepository.findAll()
                .map(UserAdminResponse::toResponse);
    }
}
