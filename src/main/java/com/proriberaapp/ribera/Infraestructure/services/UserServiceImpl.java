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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user);
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
