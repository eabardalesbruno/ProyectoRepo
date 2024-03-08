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

import java.sql.Timestamp;

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
    public Mono<UserAdminResponse> register(Integer idUserAdmin, RegisterRequest registerRequest) {

        String password = passwordEncoder.encode(registerRequest.password());

        UserAdminEntity userCreate = RegisterRequest.from(registerRequest, password);
        userCreate.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userCreate.setCreatedId(idUserAdmin);

        String username = registerRequest.firstName().toUpperCase() + " " + registerRequest.lastName().toUpperCase();

        return userAdminRepository.findByUsernameOrEmailOrDocument(username , registerRequest.email(), registerRequest.document()).hasElement()
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username or email or document already in use"))
                        : userAdminRepository.save(userCreate))
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> update(
            Integer idUserAdmin,
            Integer idUserAdminUpdate,
            UpdateUserAdminRequest updateUserAdminRequest
    ) {
        return userAdminRepository.findById(idUserAdminUpdate)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == States.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .map(user -> {
                    String username = updateUserAdminRequest.firstName().toUpperCase() + " " + updateUserAdminRequest.lastName().toUpperCase();
                    user.setEmail(user.getEmail().equals(updateUserAdminRequest.email()) ? user.getEmail() : updateUserAdminRequest.email());
                    user.setPassword(user.getPassword());
                    user.setUsername(user.getUsername().equals(username) ? user.getUsername() : username);
                    user.setFirstName(updateUserAdminRequest.firstName().toUpperCase());
                    user.setLastName(updateUserAdminRequest.lastName().toUpperCase());
                    user.setPhone(updateUserAdminRequest.phone());
                    user.setAddress(updateUserAdminRequest.address().toUpperCase());
                    user.setTypeDocument(updateUserAdminRequest.typeDocument());
                    user.setDocument(updateUserAdminRequest.document());
                    user.setRole(updateUserAdminRequest.role());
                    user.setStatus(user.getStatus());
                    user.setPermission(updateUserAdminRequest.permission());

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> updatePassword(Integer id, Integer idUserAdminUpdatePassword, String newPassword) {
        return userAdminRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == States.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<Void> delete(Integer id, Integer idUserAdminDelete) {
        return userAdminRepository.findById(id)
                .flatMap(userAdminRepository::delete);
    }

    @Override
    public Mono<UserAdminResponse> findByEmail(String email) {
        return userAdminRepository.findByEmail(email)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> disable(Integer id, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setStatus(States.INACTIVE);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> enable(Integer id, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setStatus(States.ACTIVE);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> findById(Integer id) {
        return userAdminRepository.findById(id)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Flux<UserAdminResponse> findAll(Integer idUserAdmin) {
        log.info("idUserAdmin: {}", idUserAdmin);
        return userAdminRepository.findAll()
                .map(UserAdminResponse::toResponse);
    }
}
