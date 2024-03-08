package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtil;

    @Override
    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(user.getPassword());
                    return userRepository.findByDocumentNumber(user.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(userRepository.save(user));
                }))
                .map(savedUser -> {
                    savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));
                    return savedUser;
                });
    }
    //validacion por email que no exista.
    //validacion de contraseña que tenga numeros y letras.
    //validacion que no se repita el dni.
    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new RuntimeException("La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres");
        }
    }
    @Override
    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(user.getPassword());
                    return userRepository.findByDocumentNumber(user.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(userRepository.save(user));
                }))
                .map(savedUser -> {
                    savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));
                    return savedUser;
                });
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(jwtUtil.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("Credenciales inválidas"));
                    }
                });
    }

    @Override
    public Mono<UserEntity> registerWithGoogle(String googleId, String email, String name) {
        UserEntity user = UserEntity.builder()
                .googleId(googleId)
                .email(email)
                .firstName(name)
                .build();
        return userRepository.save(user);
    }

    @Override
    public Mono<String> loginWithGoogle(String googleId) {
        return userRepository.findByGoogleId(googleId)
                .map(user -> jwtUtil.generateToken(user))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }
}
