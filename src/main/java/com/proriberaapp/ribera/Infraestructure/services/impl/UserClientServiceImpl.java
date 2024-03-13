package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.services.UserApiClient;
import com.proriberaapp.ribera.Infraestructure.services.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClientRepository userClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtil;
    @Autowired
    private UserApiClient userApiClient;

    @Override
    public Mono<UserClientEntity> registerUser(UserClientEntity user) {
        return userClientRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(user.getPassword());
                    return userClientRepository.findByDocumentNumber(user.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(user));
                }))
                .map(userToSave -> {
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); // Cifra la contraseña
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
    public Mono<UserClientEntity> saveUser(UserClientEntity user) {
        return userClientRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(user.getPassword());
                    return userClientRepository.findByDocumentNumber(user.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(user));
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
    public void updatePassword(UserClientEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userClientRepository.save(user);
    }

}
