package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.admin.exception.CustomException;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserAdminRepository;
import com.proriberaapp.ribera.services.admin.UserAdminManagerService;
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
public class UserAdminManagerServiceImpl implements UserAdminManagerService {

    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userAdminRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
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
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
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
                    user.setTypeDocument(updateUserAdminRequest.typeDocument());//TODO: revisar
                    user.setDocument(updateUserAdminRequest.document());//TODO: revisar
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
    public Mono<UserAdminResponse> updatePassword(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword) {
        return userAdminRepository.findById(idUserAdminUpdatePassword)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> updatePasswordBySolicitude(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword, String oldPassword) {
        return userAdminRepository.findById(idUserAdminUpdatePassword)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .filter(user -> passwordEncoder.matches(oldPassword, user.getPassword()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> updatePasswordByVerificationCode(String verificationCode, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> updatePasswordByVerificationCode(Integer idUserAdmin, String verificationCode, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserAdminResponse> requestUpdatePassword(RequestUpdateUserAdminRequest requestUpdateRequest) {
        return userAdminRepository.findByEmailAndDocument(requestUpdateRequest.email(), requestUpdateRequest.document())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))

                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
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

    @Override
    public Mono<UserAdminResponse> disable(Integer idUserAdmin, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(idUserAdminUpdateStatus)
                .map(user -> {
                    user.setStatus(StatesUser.INACTIVE);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> enable(Integer idUserAdmin, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(idUserAdminUpdateStatus)
                .map(user -> {
                    user.setStatus(StatesUser.ACTIVE);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }

    @Override
    public Mono<UserAdminResponse> delete(Integer id, Integer idUserAdminDelete) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setStatus(StatesUser.DELETED);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdminDelete);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserAdminResponse::toResponse);
    }
}
