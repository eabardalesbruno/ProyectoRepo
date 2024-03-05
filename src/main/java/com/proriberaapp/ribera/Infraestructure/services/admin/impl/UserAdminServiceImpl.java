package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserAdminRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userAdminRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .map(user -> new TokenDto(jwtTokenProvider.generateToken(user.getUsername())))
                .switchIfEmpty(Mono.error(new RuntimeException("bad credentials")));
    }

    @Override
    public Mono<UserAdminResponse> register(RegisterRequest userAdminEntity) {
        //todo: implementar roles
        UserAdminEntity user = RegisterRequest.from(userAdminEntity);
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
