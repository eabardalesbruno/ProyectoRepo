package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.UserApiClient;
import com.proriberaapp.ribera.services.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClientRepository userClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtUtil;
    @Autowired
    private UserApiClient userApiClient;

    /*
    @Override
    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(userClient.getPassword());
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); // Cifra la contraseña
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

     */

    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        // Obtener la fecha y hora actual
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        // Establecer la fecha y hora de creación
        userClient.setCreatedat(currentTimestamp);
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> {
                    if ("1".equals(userClient.getGoogleAuth())) {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    } else {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (!"1".equals(userClient.getGoogleAuth())) {
                        validatePassword(userClient.getPassword());
                    }
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    if (!"1".equals(userToSave.getGoogleAuth())) {
                        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    }
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new RuntimeException("La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres");
        }
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return userClientRepository.findAll();
    }
    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return userClientRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return userClientRepository.deleteById(id);
    }

    @Override
    public Mono<UserClientEntity> saveUser(UserClientEntity userClient) {
        // Obtener la fecha y hora actual
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        // Establecer la fecha y hora de creación
        userClient.setCreatedat(currentTimestamp);
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(userClient.getPassword());
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); // Cifra la contraseña
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userClientRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(jwtUtil.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("Credenciales inválidas"));
                    }
                });
    }

    @Override
    public Mono<UserClientEntity> registerWithGoogle(String googleId, String email, String name) {
        UserClientEntity user = UserClientEntity.builder()
                .googleId(googleId)
                .email(email)
                .firstName(name)
                .build();
        return userClientRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userClientRepository.findByEmail(email)
                .map(user -> true)
                .defaultIfEmpty(false)
                .block();
    }

    @Override
    public Mono<String> loginWithGoogle(String googleId) {
        return userClientRepository.findByGoogleId(googleId)
                .map(user -> jwtUtil.generateToken(user))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }

    @Override
    public UserDataDTO searchUser(String username) {
        // Llamar al cliente API para buscar el usuario por username
        return userApiClient.searchUser(username);
    }

    @Override
    public UserDataDTO registerUser(UserDataDTO userDataDTO) {
        // Registrar el usuario en la base de datos
        return userClientRepository.save(userDataDTO);
    }

    @Override
    public String loginUser(String username, String password) {
        return userApiClient.loginUser(username, password);
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> updatePassword(UserClientEntity userClient, String newPassword) {
        userClient.setPassword(passwordEncoder.encode(newPassword));
        userClientRepository.save(userClient).block();
        return null;
    }
}
