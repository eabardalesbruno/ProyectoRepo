package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.UserService;
import com.proriberaapp.ribera.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public Mono<UserEntity> registerUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(jwtUtil.generateToken(user.getEmail()));
                    } else {
                        return Mono.error(new RuntimeException("Credenciales inválidas"));
                    }
                });
    }

    @Override
    public Mono<UserEntity> registerWithGoogle(String googleId, String email, String name) {
        UserEntity user = new UserEntity();
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setFirstName(name);
        return userRepository.save(user);
    }

    @Override
    public Mono<String> loginWithGoogle(String googleId) {
        return userRepository.findByGoogleId(googleId)
                .map(user -> jwtUtil.generateToken(user.getEmail()))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }
}
