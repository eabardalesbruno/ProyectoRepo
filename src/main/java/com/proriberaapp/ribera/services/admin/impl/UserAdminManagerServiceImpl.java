package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
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
    private final JwtProvider jwtProvider;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userAdminRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")))
                .map(user -> new TokenDto(jwtProvider.generateTokenAdmin(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "error generating token")));
    }

    @Override
    public Mono<UserResponse> register(Integer idUserAdmin, RegisterRequest registerRequest) {

        String password = passwordEncoder.encode(registerRequest.password());

        UserAdminEntity userCreate = RegisterRequest.from(registerRequest, password);
        userCreate.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userCreate.setCreatedId(idUserAdmin);

        String username = registerRequest.firstName().toUpperCase() + " " + registerRequest.lastName().toUpperCase();

        return userAdminRepository.findByUsernameOrEmailOrDocumentNumber(username , registerRequest.email(), registerRequest.document()).hasElement()
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username or email or document already in use"))
                        : userAdminRepository.save(userCreate))
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> update(
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
                    user.setDocumenttypeId(updateUserAdminRequest.typeDocument());//TODO: revisar
                    user.setDocumentNumber(updateUserAdminRequest.document());//TODO: revisar
                    user.setRole(updateUserAdminRequest.role());
                    user.setStatus(user.getStatus());
                    user.setPermission(updateUserAdminRequest.permission());

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> updatePassword(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword) {
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
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> updatePasswordBySolicitude(Integer idUserAdmin, Integer idUserAdminUpdatePassword, String newPassword, String oldPassword) {
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
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> updatePasswordByVerificationCode(String verificationCode, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserResponse> updatePasswordByVerificationCode(Integer idUserAdmin, String verificationCode, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserResponse> requestUpdatePassword(RequestUpdateUserAdminRequest requestUpdateRequest) {
        return userAdminRepository.findByEmailAndDocumentNumber(requestUpdateRequest.email(), requestUpdateRequest.document())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))

                .flatMap(userAdminRepository::save)
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return userAdminRepository.findByEmail(email)
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> findById(Integer id) {
        return userAdminRepository.findById(id)
                .map(UserResponse::toResponse);
    }

    @Override
    public Flux<UserResponse> findAll() {
        return userAdminRepository.findAll()
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> disable(Integer idUserAdmin, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(idUserAdminUpdateStatus)
                .map(user -> {
                    user.setStatus(StatesUser.INACTIVE);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> enable(Integer idUserAdmin, Integer idUserAdminUpdateStatus) {
        return userAdminRepository.findById(idUserAdminUpdateStatus)
                .map(user -> {
                    user.setStatus(StatesUser.ACTIVE);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdmin);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserResponse::toResponse);
    }

    @Override
    public Mono<UserResponse> delete(Integer id, Integer idUserAdminDelete) {
        return userAdminRepository.findById(id)
                .map(user -> {
                    user.setStatus(StatesUser.DELETED);

                    user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    user.setUpdatedId(idUserAdminDelete);
                    return user;
                })
                .flatMap(userAdminRepository::save)
                .map(UserResponse::toResponse);
    }
}
